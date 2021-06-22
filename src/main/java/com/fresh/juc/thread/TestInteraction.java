package com.fresh.juc.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @author guowenyu
 * @since 2021/6/22
 */
@Slf4j
public class TestInteraction {

    private static final Object sLock = new Object();
    private static boolean t2NotOver = true;

    /**
     * 先打印 2，再打印1
     * @param args
     */
    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (sLock) {
                while (t2NotOver) {
                    try {
                        sLock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info("1");
            }
        }, "t1").start();

        new Thread(() -> {
            synchronized (sLock) {
                log.info("2");
                t2NotOver = false;
                sLock.notifyAll();
            }
        }, "t2").start();
    }

}
