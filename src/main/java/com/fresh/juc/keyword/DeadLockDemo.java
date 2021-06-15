package com.fresh.juc.keyword;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

/**
 * @author guowenyu
 * @since 2021/6/8
 */
@Slf4j
public class DeadLockDemo {

    static Object lockA = new Object();
    static Object lockB = new Object();

    public static void main(String[] args) {

        new Thread(() -> {
            synchronized (lockA) {
                log.info("get lockA");
                LockSupport.parkUntil(1000);
                synchronized (lockB) {
                    log.info("get lockB");
                }
            }
        }, "A").start();


        new Thread(() -> {
            synchronized (lockB) {
                log.info("get lockB");
                LockSupport.parkUntil(1000);
                synchronized (lockA) {
                    log.info("get lockA");
                }
            }
        }, "B").start();
    }

}
