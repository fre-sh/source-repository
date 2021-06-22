package com.fresh.juc.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author guowenyu
 * @since 2021/6/22
 */
@Slf4j
public class TestInteraction {

    private static final Object sLock = new Object();
    private static boolean t2NotOver = true;

    /**
     * 先打印 2，再打印1, park实现
     * @param args
     */
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            LockSupport.park();
            log.info("1");
        }, "t1");
        t1.start();

        new Thread(() -> {
            log.info("2");
            LockSupport.unpark(t1);
        }, "t2").start();
    }

}
