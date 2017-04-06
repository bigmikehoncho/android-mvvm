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

package com.manaschaudhari.android_mvvm.sample.two_way_binding;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.support.annotation.NonNull;

import com.manaschaudhari.android_mvvm.INavigator;
import com.manaschaudhari.android_mvvm.ViewModel;
import com.manaschaudhari.android_mvvm.sample.BaseVM;
import com.manaschaudhari.android_mvvm.sample.Item;
import com.manaschaudhari.android_mvvm.sample.ItemViewModel;
import com.manaschaudhari.android_mvvm.sample.Navigator;
import com.manaschaudhari.android_mvvm.sample.adapters.MessageHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.functions.Action;

public class SearchViewModel extends BaseVM<INavigator> {
    public final ObservableField<String> searchQuery = new ObservableField<>("");
    public final ObservableList<ViewModel> results = new ObservableArrayList<>();
    public final Action onRandomSearch;
    
    public SearchViewModel(@NonNull final MessageHelper messageHelper, @NonNull final Navigator navigator) {
        searchQuery.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(android.databinding.Observable observable, int i) {
                results.clear();
                String s = searchQuery.get();
                if (s.length() > 0) {
                    String[] words = s.split(" ");
                    for (String word : words) {
                        results.add(new ItemViewModel(new Item(word), messageHelper, navigator));
                    }
                }
            }
        });
        
        onRandomSearch = new Action() {
            @Override
            public void run() {
                String randomString = UUID.randomUUID().toString();
                String randomQuery = randomString.substring(0, 3) + " " + randomString.substring(5, 8);
                searchQuery.set(randomQuery);
            }
        };
    }
}
