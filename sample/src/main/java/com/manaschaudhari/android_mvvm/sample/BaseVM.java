package com.manaschaudhari.android_mvvm.sample;

import android.support.annotation.CallSuper;

import com.manaschaudhari.android_mvvm.INavigator;
import com.manaschaudhari.android_mvvm.ViewModel;

/**
 * Convenience view-model from which other view-models can extend
 */
public abstract class BaseVM<N extends INavigator> implements ViewModel<N> {
    
    protected N navigator;
    
    @CallSuper
    @Override
    public void setNavigator(N navigator) {
        this.navigator = navigator;
    }
    
    @Override
    public void onDestroy() {
        
    }
}
