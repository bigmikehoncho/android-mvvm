package com.manaschaudhari.android_mvvm;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manaschaudhari.android_mvvm.adapters.ViewModelBinder;
import com.manaschaudhari.android_mvvm.utils.BindingUtils;
import com.manaschaudhari.android_mvvm.utils.Preconditions;

/**
 * Inflates the provided view and binds the provided ViewModel based on default
 * binder provided to the library
 */
public abstract class MvvmFragment extends Fragment {

    private ViewDataBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        getDefaultBinder().bind(binding, createViewModel());
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        getDefaultBinder().bind(binding, null);
        binding.executePendingBindings();
        super.onDestroyView();
    }


    @NonNull
    private ViewModelBinder getDefaultBinder() {
        ViewModelBinder defaultBinder = BindingUtils.getDefaultBinder();
        Preconditions.checkNotNull(defaultBinder, "Default Binder");
        return defaultBinder;
    }

    @NonNull
    protected abstract ViewModel createViewModel();

    @LayoutRes
    protected abstract int getLayoutId();
}
