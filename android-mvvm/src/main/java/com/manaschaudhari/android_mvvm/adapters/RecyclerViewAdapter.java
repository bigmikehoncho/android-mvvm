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

package com.manaschaudhari.android_mvvm.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.manaschaudhari.android_mvvm.ViewModel;

public class RecyclerViewAdapter<VM extends ViewModel> extends RecyclerView.Adapter<RecyclerViewAdapter.DataBindingViewHolder> {
    private final
    @NonNull
    ViewProvider viewProvider;
    private final
    @NonNull
    ViewModelBinder binder;
    private final
    @NonNull
    ObservableList<VM> source;
    private final
    @NonNull
    ObservableList.OnListChangedCallback<ObservableList<VM>> onListChangedCallback = new ObservableList.OnListChangedCallback<ObservableList<VM>>() {
        @Override
        public void onChanged(ObservableList<VM> viewModels) {
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(ObservableList<VM> viewModels, int start, int count) {
            notifyItemRangeChanged(start, count);
        }

        @Override
        public void onItemRangeInserted(ObservableList<VM> viewModels, int start, int count) {
            notifyItemRangeInserted(start, count);
        }

        @Override
        public void onItemRangeMoved(ObservableList<VM> viewModels, int from, int to, int count) {
            notifyItemRangeRemoved(from, count);
            notifyItemRangeInserted(to, count);
        }

        @Override
        public void onItemRangeRemoved(ObservableList<VM> viewModels, int start, int count) {
            notifyItemRangeRemoved(start, count);
        }
    };

    public RecyclerViewAdapter(@NonNull ObservableList<VM> viewModels,
                               @NonNull ViewProvider viewProvider,
                               @NonNull ViewModelBinder viewModelBinder) {
        this.viewProvider = viewProvider;
        this.binder = viewModelBinder;
        source = viewModels;
    }

    @Override
    public int getItemViewType(int position) {
        return viewProvider.getView(source.get(position));
    }

    @Override
    public DataBindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), viewType, parent, false);
        return new DataBindingViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(DataBindingViewHolder holder, int position) {
        VM vm = source.get(position);
        holder.viewBinding.getRoot().setTag(vm);
        binder.bind(holder.viewBinding, vm);
        holder.viewBinding.executePendingBindings();
    }
    
    @Override
    public void onViewRecycled(DataBindingViewHolder holder) {
        binder.bind(holder.viewBinding, null);
        holder.viewBinding.executePendingBindings();
    }
    
    @Override
    public int getItemCount() {
        return source.size();
    }
    
    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        source.addOnListChangedCallback(onListChangedCallback);
        super.registerAdapterDataObserver(observer);
    }
    
    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.unregisterAdapterDataObserver(observer);
        
        source.removeOnListChangedCallback(onListChangedCallback);
    }
    
    public static class DataBindingViewHolder extends RecyclerView.ViewHolder {
        @NonNull
        final ViewDataBinding viewBinding;
        
        public DataBindingViewHolder(@NonNull ViewDataBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }
}
