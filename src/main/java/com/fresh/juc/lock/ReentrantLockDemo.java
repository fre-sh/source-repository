package com.fresh.juc.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

/**
 * @author guowenyu
 * @since 2021/6/9
 */
@Slf4j
public class ReentrantLockDemo {

    Lock lock = new ReentrantLock();
    Condition condition1 = lock.newCondition();

    @Test
    public void testInterrupt(){
        new Thread(() -> {
            lock.lock();
        }, "").start();
    }

    @Test
    public void testCondition() throws InterruptedException {
        new Thread(() -> {
            lock.lock();
            try {
                log.info("get lock and wait");
                condition1.await();
                log.info("get lock 2 and resume");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }, "th1").start();

        sleep(500);
        log.info("try get lock");
        lock.lock();
        try {
            log.info("got lock");
            condition1.signalAll();
            log.info("signal condition1");
        } finally {
            lock.unlock();
        }
    }

}
