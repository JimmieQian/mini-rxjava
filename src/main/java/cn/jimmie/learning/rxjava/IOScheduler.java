package cn.jimmie.learning.rxjava;

import cn.jimmie.learning.rxjava.interfaces.Scheduler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * FUCTION :
 * Created by jimmie.qian on 2018/11/5.
 */
public class IOScheduler implements Scheduler {
    private final ExecutorService executor = Executors.newCachedThreadPool();

    private final Map<Runnable, Future> futureMap = new ConcurrentHashMap<>();

    @Override
    public void submit(Runnable runnable) {
        Future future = futureMap.get(runnable);
        if (future != null) {
            if (!future.isDone()) return;
        }
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
