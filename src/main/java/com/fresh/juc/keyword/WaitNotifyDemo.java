package com.fresh.juc.keyword;

import java.util.concurrent.TimeUnit;

/**
 * @author guowenyu
 * @since 2021/6/4
 */
public class WaitNotifyDemo {

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        new Thread(() -> {
            synchronized (lock) {
                try {
                    System.out.println(Thread.currentThread().getName() + "get lock and wait");
                    lock.wait(2000);
                    System.out.println(Thread.currentThread().getName() + "wait end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t2").start();

        TimeUnit.MICROSECONDS.sleep(1000);
        synchronized (lock) {
            synchronized (lock) {
                System.out.println(Thread.currentThread().getName() + "get lock and sleep");
                Thread.sleep(3000);
                System.out.println(Thread.currentThread().getName() + "free lock");
            }
        }
    }

}
