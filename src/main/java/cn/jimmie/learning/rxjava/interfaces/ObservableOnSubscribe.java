package cn.jimmie.learning.rxjava.interfaces;

// ObservableOnSubscribe.java
// Observable的事件分发接口
public interface ObservableOnSubscribe<T> {
    void subscribe(Emitter<T> emitter) throws Exception;
}
