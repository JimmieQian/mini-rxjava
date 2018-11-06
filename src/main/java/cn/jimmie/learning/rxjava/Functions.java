package cn.jimmie.learning.rxjava;


import cn.jimmie.learning.rxjava.interfaces.Action;
import cn.jimmie.learning.rxjava.interfaces.Consumer;

// Functions.java
public class Functions {
    public static final Action EMPTY_ACTION = () -> {};
    public static <T> Consumer<T> emptyConsumer() {
        return t -> {};
    }
}
