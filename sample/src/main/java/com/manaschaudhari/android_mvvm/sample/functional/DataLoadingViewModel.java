/*
 * Copyright 2016 Manas Chaudhari
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.manaschaudhari.android_mvvm.sample.functional;

import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import com.manaschaudhari.android_mvvm.ReadOnlyField;
import com.manaschaudhari.android_mvvm.ViewModel;
import com.manaschaudhari.android_mvvm.sample.utils.RxUtils;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

import static com.manaschaudhari.android_mvvm.FieldUtils.toField;

public class DataLoadingViewModel implements ViewModel {

    @NonNull
    public final ReadOnlyField<String> result;

    @NonNull
    public final ReadOnlyField<Boolean> progressVisible;

    @NonNull
    public final ReadOnlyField<Boolean> errorVisible;

    public DataLoadingViewModel(@NonNull DataService service) {
        Single<String> data = service.loadData(); // Try service.loadData_Fail(); for error scenario
        final Observable<String> cachedData = data.toObservable()
                .onErrorReturn(new Function<Throwable, String>() {
                    @Override
                    public String apply(Throwable throwable) throws Exception {
                        return null;
                    }
                }).cache();

        Pair<Observable<String>, Observable<Boolean>> tracker = RxUtils.trackActivity(cachedData);

        result = toField(tracker.first);
        this.progressVisible = toField(tracker.second);
        this.errorVisible = toField(Observable.combineLatest(tracker.first, tracker.second, new BiFunction<String, Boolean, Boolean>() {
            @Override
            public Boolean apply(String result, Boolean inProgress) throws Exception {
                return !inProgress && result == null;
            }
        }));
    }

    @Override
    public void onDestroy() {

    }
}
