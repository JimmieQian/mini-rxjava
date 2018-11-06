import cn.jimmie.learning.rxjava.Functions;
import cn.jimmie.learning.rxjava.Observable;
import cn.jimmie.learning.rxjava.Schedulers;
import cn.jimmie.learning.rxjava.interfaces.Disposable;
import cn.jimmie.learning.rxjava.interfaces.ObservableOnSubscribe;

import java.util.concurrent.TimeUnit;

/**
 * FUCTION : 测试代码
 * Created by jimmie.qian on 2018/11/5.
 */
public class Test {
    @org.junit.Test
    public void schedulerTest() throws InterruptedException {
        Observable.create((ObservableOnSubscribe<String>) emmit -> {
            System.out.println("emmit : " + Thread.currentThread().getName());
            emmit.onNext("1");
            emmit.onNext("2");
            emmit.onComplete();
        })
                .observeOn(Schedulers.io())
                .map(it -> {
                    int r = Integer.parseInt(it) * 10;
                    System.out.println(r + " : map : " + Thread.currentThread().getName());
                    return r;
                }).subscribe(it -> System.out.println(it + " : observer : " + Thread.currentThread().getName()),
                Functions.emptyConsumer(),
                () -> System.out.println("onCompleted.... " + Thread.currentThread().getName()));
        TimeUnit.SECONDS.sleep(1);
    }

    @org.junit.Test
    public void mapTest() {
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            emitter.onNext("1");
            emitter.onNext("2");
            emitter.onComplete();
        })
                .map(s -> Integer.parseInt(s) * 10)
                .subscribe(System.out::println);
    }


    private Disposable mDisposable = null;

    @org.junit.Test
    public void disposableTest() {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onComplete();
        })
                .subscribe(integer -> {
                            System.out.println(integer);
                            if (integer == 2 && mDisposable != null)
                                mDisposable.dispose();
                        },
                        Functions.emptyConsumer(), Functions.EMPTY_ACTION,
                        d -> mDisposable = d);
    }

    @org.junit.Test
    public void disposableTest2() {
        Disposable disposable = Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onComplete();
        })
                .observeOn(Schedulers.io())
                .subscribe(System.out::println);
        disposable.dispose();
    }
}
