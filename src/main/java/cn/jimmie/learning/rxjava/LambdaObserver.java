package cn.jimmie.learning.rxjava;


import cn.jimmie.learning.rxjava.interfaces.Action;
import cn.jimmie.learning.rxjava.interfaces.Consumer;
import cn.jimmie.learning.rxjava.interfaces.Disposable;
import cn.jimmie.learning.rxjava.interfaces.Observer;

// LambdaObserver.java
public class LambdaObserver<T> implements Observer<T>, Disposable {
    private final Consumer<? super T>          onNext;
    private final Consumer<? super Throwable>  onError;
    private final Action                       onComplete;
    private final Consumer<? super Disposable> onSubscribe;
    LambdaObserver(Consumer<? super T> onNext, Consumer<? super Throwable> onError,
                   Action onComplete, Consumer<? super Disposable> onSubscribe) {
        this.onNext = onNext;
        this.onError = onError;
        this.onComplete = onComplete;
        this.onSubscribe = onSubscribe;
    }
    @Override
    public void onSubscribe(Disposable d) {
        try {
            onSubscribe.apply(d);
        } catch (Exception e) {
            onError(e);
        }
    }
    @Override
    public void onNext(T t) {
        if (!isDisposed())
            try {
                onNext.apply(t);
            } catch (Exception e) {
                onError(e);
            }
    }
    @Override
    public void onError(Throwable e) {
        if (!isDisposed())
            try {
                onError.apply(e);
            } catch (Exception e1) {
                e1.printStackTrace();
            } finally {
                dispose();
            }
    }
    @Override
    public void onComplete() {
        if (!isDisposed())
            try {
                onComplete.apply();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                dispose();
            }
    }
    private volatile boolean isDisposed = false;
    @Override
    public void dispose() {
        isDisposed = true;
    }
    @Override
    public boolean isDisposed() {
        return isDisposed;
    }
}
