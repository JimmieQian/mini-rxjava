package cn.jimmie.learning.rxjava.interfaces;

/**
 * FUCTION :
 * Created by jimmie.qian on 2018/11/5.
 */
// Function.java
// 接收一个类型的参数,返回一个类型
public interface Function<T, R> {
    R apply(T t) throws Exception;
}
