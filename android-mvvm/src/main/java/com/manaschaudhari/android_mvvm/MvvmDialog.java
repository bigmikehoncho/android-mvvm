package com.manaschaudhari.android_mvvm;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import com.manaschaudhari.android_mvvm.adapters.ViewModelBinder;
import com.manaschaudhari.android_mvvm.utils.BindingUtils;
import com.manaschaudhari.android_mvvm.utils.Preconditions;

/**
 * Created by michael on 2/8/2017.
 */

public abstract class MvvmDialog extends DialogFragment {
    protected ViewDataBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), getLayoutId(), null, false);
        getDefaultBinder().bind(binding, createViewModel());
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
