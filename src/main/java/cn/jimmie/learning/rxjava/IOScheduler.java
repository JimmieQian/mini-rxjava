package cn.jimmie.learning.rxjava;

import cn.jimmie.learning.rxjava.interfaces.Scheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// io线程调度的具体实现
public class IOScheduler implements Scheduler {
    // 线程池
    private final ExecutorService       executor  = Executors.newCachedThreadPool();
    // 保存Future对象,为了能够中断指定线程
    private final Map<Runnable, Future> futureMap = new ConcurrentHashMap<>();
    @Override
    public void submit(Runnable runnable) {
        Future future = futureMap.get(runnable);
        // 如果对应的任务正在执行,则无需再提交
        if (future != null && !future.isDone()) return;
        if (executor.isShutdown()) return;
        futureMap.put(runnable, executor.submit(runnable));
    }
    @Override
    public void remove(Runnable runnable) {
        Future future = futureMap.get(runnable);
        if (future == null) return;
        try {
            future.cancel(true);
        } catch (Exception ignored) {
        } finally {
            futureMap.remove(runnable);
        }
    }
    @Override
    public void shutdown() {
        if (!executor.isShutdown())
            executor.shutdown();
    }
}
