package cn.jimmie.learning.rxjava.interfaces;

// Emitter.java
public interface Emitter<T> {
    void onNext(T t);
    void onError(Throwable e);
    void onComplete();
}
