package cn.jimmie.learning.rxjava;


import cn.jimmie.learning.rxjava.interfaces.Disposable;
import cn.jimmie.learning.rxjava.interfaces.Emitter;
import cn.jimmie.learning.rxjava.interfaces.ObservableOnSubscribe;
import cn.jimmie.learning.rxjava.interfaces.Observer;

// ObservableCreate.java
// Observable的一个适配器,用于快速创建一个可以发送事件的Observable
final class ObservableCreate<T> extends Observable<T> {
    // 事件分发接口
    private final ObservableOnSubscribe<T> source;

    ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
        // 分发逻辑代码
    void subscribeActual(Observer<? super T> observer) {
        CreateEmitter<T> emitter = new CreateEmitter<>(observer);
        // 将中断器回调给observer
        observer.onSubscribe(emitter);
        try {
            source.subscribe(emitter);
        } catch (Exception e) {
            // 异常接收和处理
            emitter.onError(e);
        }
    }
    // 内部分发器
    static class CreateEmitter<T> implements Emitter<T>, Disposable {
        private final Observer<? super T> observer;
        CreateEmitter(Observer<? super T> observer) {
            this.observer = observer;
        }
        @Override
        public void onNext(T t) {
            // 如果事件没被消费,则进行操作
            if (!isDisposed())
                observer.onNext(t);
        }
        @Override
        public void onError(Throwable e) {
            if (!isDisposed()) {
                try {
                    observer.onError(e);
                } finally {
                    // 触发消费,后续不再处理事件
                    dispose();
                }
            }
        }
        @Override
        public void onComplete() {
            if (!isDisposed()) {
                try {
                    observer.onComplete();
                } finally {
                    // 触发消费,后续不再处理事件
                    dispose();
                }
            }
        }
        private volatile boolean isDisposed = false;
        @Override
        public void dispose() {
            isDisposed = true;
        }
        @Override
        public boolean isDisposed() {
            return isDisposed;
        }
    }
}
