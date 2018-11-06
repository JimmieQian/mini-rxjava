package cn.jimmie.learning.rxjava;


import cn.jimmie.learning.rxjava.interfaces.Disposable;
import cn.jimmie.learning.rxjava.interfaces.ObservableSource;
import cn.jimmie.learning.rxjava.interfaces.Observer;
import cn.jimmie.learning.rxjava.interfaces.Scheduler;

import java.util.LinkedList;
import java.util.Queue;

// 实现线程调度的Observable
public final class ObservableObserveOn<T> extends Observable<T> {
    private final ObservableSource<T> source;
    private final Scheduler           scheduler;

    ObservableObserveOn(ObservableSource<T> source, Scheduler scheduler) {
        this.source = source;
        this.scheduler = scheduler;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        source.subscribe(new ObserveOnObserver<>(observer, scheduler));
    }

    static final class ObserveOnObserver<T> implements Observer<T>, Disposable, Runnable {
        private final    Observer<? super T> downstream;
        private final    Scheduler           scheduler;
        private          Disposable          upstream;
        private volatile boolean             done;
        private volatile boolean             disposed;
        private          Queue<T>            queue = new LinkedList<>();
        private          Throwable           error;

        ObserveOnObserver(Observer<? super T> actual, Scheduler scheduler) {
            this.downstream = actual;
            this.scheduler = scheduler;
        }

        @Override
        public void onSubscribe(Disposable d) {
            upstream = d;
            downstream.onSubscribe(this);
        }

        @Override
        public void onNext(T t) {
            if (done) return;
            queue.offer(t);
            schedule();
        }

        @Override
        public void onError(Throwable t) {
            if (done) return;
            done = true;
            error = t;
            schedule();
        }

        @Override
        public void onComplete() {
            if (done) return;
            done = true;
            schedule();
        }

        @Override
        public void dispose() {
            if (!disposed) {
                disposed = true;
                upstream.dispose();
                scheduler.remove(this);
                queue.clear();
            }
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }

        // 提交任务
        void schedule() {
            scheduler.submit(this);
        }

        // 检查事件是否已中断,并作出相应的反馈
        boolean checkTerminated(boolean d, boolean empty, Observer<? super T> a) {
            if (disposed) {
                queue.clear();
                return true;
            }
            if (d) {
                Throwable e = error;
                if (e != null) {
                    disposed = true;
                    queue.clear();
                    a.onError(e);
                    scheduler.remove(this);
                    return true;
                } else if (empty) {
                    disposed = true;
                    a.onComplete();
                    scheduler.remove(this);
                    return true;
                }
            }
            return false;
        }

        @Override // 拦截事件传递,到run方法,run方法将由线程池运行
        public void run() {
            final Queue<T>            q = queue;
            final Observer<? super T> a = downstream;
            for (; ; ) {
                if (checkTerminated(done, q.isEmpty(), a)) return;
                for (; ; ) {
                    boolean d = done;
                    T       v;
                    try {
                        v = q.poll();
                    } catch (Throwable ex) {
                        disposed = true;
                        upstream.dispose();
                        q.clear();
                        a.onError(ex);
                        scheduler.remove(this);
                        return;
                    }
                    boolean empty = v == null;
                    if (checkTerminated(d, empty, a)) return;
                    if (empty) break;
                    a.onNext(v);
                }
            }
        }
    }
}

