package com.manaschaudhari.android_mvvm;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by michael on 2/15/2017.
 */

public class ReadOnlyBoolean extends ObservableBoolean {
    final Observable<Boolean> source;
    final HashMap<OnPropertyChangedCallback, Disposable> disposables = new HashMap<>();

    public static ReadOnlyBoolean create(@NonNull Observable<Boolean> source) {
        return new ReadOnlyBoolean(source);
    }

    protected ReadOnlyBoolean(@NonNull Observable<Boolean> source) {
        super();
        this.source = source
                .doOnNext(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean t) throws Exception {
                        ReadOnlyBoolean.super.set(t);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ReadOnlyField", "onError in source observable", throwable);
                    }
                })
                .onErrorResumeNext(Observable.<Boolean>empty())
                .share();
    }

    /**
     * @deprecated Setter of ReadOnlyField does nothing. Merge with the source Observable instead.
     * See <a href="https://github.com/manas-chaudhari/android-mvvm/tree/master/Documentation/ObservablesAndSetters.md">Documentation/ObservablesAndSetters.md</a>
     */
    @Deprecated
    @Override
    public void set(boolean value) {}

    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.addOnPropertyChangedCallback(callback);
        disposables.put(callback, source.subscribe());
    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.removeOnPropertyChangedCallback(callback);
        Disposable disposable = disposables.remove(callback);
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
