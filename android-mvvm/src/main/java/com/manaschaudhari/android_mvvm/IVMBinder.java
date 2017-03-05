package com.manaschaudhari.android_mvvm;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;

/**
 * Created by mike on 3/4/17.
 */

public interface IVMBinder {
    
    @NonNull
    ViewModel createViewModel();
    
    @LayoutRes
    int getLayoutId();
}
