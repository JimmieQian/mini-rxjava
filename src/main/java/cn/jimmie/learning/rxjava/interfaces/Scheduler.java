package cn.jimmie.learning.rxjava.interfaces;

/**
 * FUCTION :
 * Created by jimmie.qian on 2018/11/5.
 */
public interface Scheduler {

    void submit(Runnable runnable);

    void remove(Runnable runnable);

    void shutdown();
}
