package cn.jimmie.learning.rxjava;

import cn.jimmie.learning.rxjava.interfaces.Scheduler;

// 线程调度工厂
public class Schedulers {
    public static Scheduler io() {
        return IOHolder.DEFAULT;
    }

    // 内部静态类,实现懒加载
    private final static class IOHolder {
        private static final Scheduler DEFAULT = new IOScheduler();
    }
}
