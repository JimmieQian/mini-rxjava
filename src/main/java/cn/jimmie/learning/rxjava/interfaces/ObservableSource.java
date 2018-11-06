package cn.jimmie.learning.rxjava.interfaces;
// ObservableSource.java
// 被观察者(主题)接口
public interface ObservableSource<T> {
    void subscribe(Observer<? super T> observer);
}
