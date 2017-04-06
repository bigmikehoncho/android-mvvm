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

package com.manaschaudhari.android_mvvm.utils;

import android.databinding.BindingAdapter;
import android.databinding.BindingConversion;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.manaschaudhari.android_mvvm.ViewModel;
import com.manaschaudhari.android_mvvm.adapters.Connectable;
import com.manaschaudhari.android_mvvm.adapters.RecyclerViewAdapter;
import com.manaschaudhari.android_mvvm.adapters.ViewModelBinder;
import com.manaschaudhari.android_mvvm.adapters.ViewPagerAdapter;
import com.manaschaudhari.android_mvvm.adapters.ViewProvider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

@SuppressWarnings("unused")
public class BindingUtils {
    
    @Nullable
    private static ViewModelBinder defaultBinder = null;
    
    @Nullable
    public static ViewModelBinder getDefaultBinder() {
        return defaultBinder;
    }
    
    public static void setDefaultBinder(@NonNull ViewModelBinder viewModelBinder) {
        defaultBinder = viewModelBinder;
    }
    
    @BindingAdapter("adapter")
    public static void bindAdapter(@NonNull ViewPager viewPager, @Nullable PagerAdapter adapter) {
        PagerAdapter oldAdapter = viewPager.getAdapter();
        
        // Disconnect previous adapter if its Connectable
        if (oldAdapter != null && oldAdapter instanceof Connectable) {
            ((Connectable) oldAdapter).removeCallback();
        }
        
        // Connect the new adapter
        if (adapter != null && adapter instanceof Connectable) {
            ((Connectable) adapter).connect();
        }
        viewPager.setAdapter(adapter);
    }
    
    @BindingAdapter("adapter")
    public static void bindAdapter(@NonNull RecyclerView recyclerView, @Nullable RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }
    
    @BindingAdapter({"items", "view_provider"})
    public static void bindAdapterWithDefaultBinder(@NonNull RecyclerView recyclerView, @Nullable ObservableList<ViewModel> items, @Nullable ViewProvider viewProvider) {
        RecyclerViewAdapter adapter = null;
        if (items != null && viewProvider != null) {
            Preconditions.checkNotNull(defaultBinder, "Default Binder");
            adapter = new RecyclerViewAdapter(items, viewProvider, defaultBinder);
        }
        bindAdapter(recyclerView, adapter);
    }
    
    @BindingAdapter({"items", "view_provider"})
    public static void bindAdapterWithDefaultBinder(@NonNull ViewPager viewPager, @Nullable ObservableList<ViewModel> items, @Nullable ViewProvider viewProvider) {
        ViewPagerAdapter adapter = null;
        if (items != null && viewProvider != null) {
            Preconditions.checkNotNull(defaultBinder, "Default Binder");
            adapter = new ViewPagerAdapter(items, viewProvider, defaultBinder);
        }
        bindAdapter(viewPager, adapter);
    }
    
    @BindingConversion
    @NonNull
    public static ViewProvider getViewProviderForStaticLayout(@LayoutRes final int layoutId) {
        return new ViewProvider() {
            @Override
            public int getView(ViewModel vm) {
                return layoutId;
            }
        };
    }
    
    @BindingConversion
    @Nullable
    public static <T extends ViewModel> Observable<List<ViewModel>> toGenericList(@Nullable Observable<List<T>> specificList) {
        return specificList == null ? null : specificList
                .map(new Function<List<T>, List<ViewModel>>() {
                    @Override
                    public List<ViewModel> apply(List<T> ts) throws Exception {
                        return new ArrayList<ViewModel>(ts);
                    }
                });
    }
    
    @BindingConversion
    @Nullable
    public static <T extends ViewModel> ObservableList<ViewModel> toObservableList(@Nullable List<T> specificList) {
        if (specificList == null) {
            return null;
        } else {
            ObservableList<ViewModel> observableList = new ObservableArrayList<>();
            observableList.addAll(specificList);
            return observableList;
        }
    }
    
    // Extra Utilities
    
    @BindingAdapter(value = {"layout_vertical", "reverse_layout"}, requireAll = false)
    public static void bindLayoutManager(@NonNull RecyclerView recyclerView, boolean vertical, boolean reverseLayout) {
        int orientation = vertical ? RecyclerView.VERTICAL : RecyclerView.HORIZONTAL;
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), orientation, reverseLayout));
    }
}
