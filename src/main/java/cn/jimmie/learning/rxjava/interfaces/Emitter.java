package cn.jimmie.learning.rxjava.interfaces;

// Emitter.java
// 事件分发接口
public interface Emitter<T> {
    void onNext(T t);

    void onError(Throwable e);

    void onComplete();
}
