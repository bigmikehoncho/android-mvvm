package com.manaschaudhari.android_mvvm;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import com.manaschaudhari.android_mvvm.adapters.ViewModelBinder;
import com.manaschaudhari.android_mvvm.utils.BindingUtils;
import com.manaschaudhari.android_mvvm.utils.Preconditions;

/**
 * Created by mike on 3/3/17.
 */

public class MvvmBinder<Binding extends ViewDataBinding> {
    protected Binding binding;
    private @LayoutRes int layoutId;
    private ViewModel viewModel;
    
    public MvvmBinder(@LayoutRes int layoutId, ViewModel viewModel) {
        this.layoutId = layoutId;
        this.viewModel = viewModel;
    }
    
    /**
     * Call after super.onCreate of activity
     * @param activity
     */
    public void onCreate(Activity activity){
        binding = DataBindingUtil.setContentView(activity, layoutId);
        getDefaultBinder().bind(binding, viewModel);
    }
    
    public void onCreate(Context context){
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, null, false);
        getDefaultBinder().bind(binding, viewModel);
    }
    
    /**
     * call before super.onDestroy or super.onDestroyView
     */
    public void onDestroy(){
        getDefaultBinder().bind(binding, null);
        binding.executePendingBindings();
    }
    
    @NonNull
    private ViewModelBinder getDefaultBinder() {
        ViewModelBinder defaultBinder = BindingUtils.getDefaultBinder();
        Preconditions.checkNotNull(defaultBinder, "Default Binder");
        return defaultBinder;
    }
    
    public Binding getBinding(){
        return binding;
    }
}
