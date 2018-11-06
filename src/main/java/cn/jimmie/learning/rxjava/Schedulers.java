package cn.jimmie.learning.rxjava;

import cn.jimmie.learning.rxjava.interfaces.Scheduler;

/**
 * FUCTION :
 * Created by jimmie.qian on 2018/11/5.
 */
public class Schedulers {
    public static final Scheduler IO = new IOScheduler();
}
