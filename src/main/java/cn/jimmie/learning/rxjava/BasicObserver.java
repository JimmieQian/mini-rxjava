package cn.jimmie.learning.rxjava;


import cn.jimmie.learning.rxjava.interfaces.Disposable;
import cn.jimmie.learning.rxjava.interfaces.Observer;

// BasicObserver.java
public abstract class BasicObserver<T, R> implements Observer<T>, Disposable {
    // 上游传递的Disposable对象
    private Disposable          upstream;
    // 下游接收的观察者对象
    final   Observer<? super R> downstream;
    // 如果已经中断,则无需下传
    boolean done;

    BasicObserver(Observer<? super R> downstream) {
        this.downstream = downstream;
    }

    @Override
    public void onSubscribe(Disposable d) {
        // 接收上游的Disposable
        this.upstream = d;
        downstream.onSubscribe(this);
    }

    @Override
    public void onError(Throwable e) {
        if (done) return;
        done = true;
        downstream.onError(e);
    }

    @Override
    public void onComplete() {
        if (done) return;
        done = true;
        downstream.onComplete();
    }

    @Override
    public void dispose() {
        upstream.dispose();
    }

    @Override
    public boolean isDisposed() {
        return upstream.isDisposed();
    }
}
