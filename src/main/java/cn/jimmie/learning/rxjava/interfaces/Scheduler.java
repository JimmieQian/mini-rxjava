package cn.jimmie.learning.rxjava.interfaces;

// 线程调度接口
public interface Scheduler {
    void submit(Runnable runnable);

    void remove(Runnable runnable);

    void shutdown();
}
