package com.manaschaudhari.android_mvvm;

import android.databinding.ObservableInt;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by michael on 2/15/2017.
 */

public class ReadOnlyInt extends ObservableInt {
    final Observable<Integer> source;
    final HashMap<OnPropertyChangedCallback, Disposable> disposables = new HashMap<>();

    public static ReadOnlyInt create(@NonNull Observable<Integer> source) {
        return new ReadOnlyInt(source);
    }

    protected ReadOnlyInt(@NonNull Observable<Integer> source) {
        super();
        this.source = source
                .doOnNext(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer t) throws Exception {
                        ReadOnlyInt.super.set(t);
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.e("ReadOnlyInt", "onError in source observable", throwable);
                    }
                })
                .onErrorResumeNext(Observable.<Integer>empty())
                .share();
    }

    /**
     * @deprecated Setter of ReadOnlyInt does nothing. Merge with the source Observable instead.
     * See <a href="https://github.com/manas-chaudhari/android-mvvm/tree/master/Documentation/ObservablesAndSetters.md">Documentation/ObservablesAndSetters.md</a>
     */
    @Deprecated
    @Override
    public void set(int value) {
    }

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
