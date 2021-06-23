package com.fresh.juc.thread;

import com.fresh.util.ThreadUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author guowenyu
 * @since 2021/6/22
 */
@Slf4j
public class TestInteraction {

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    /**
     * 0,1,2
     */
    private int status;

    /**
     * 线程1打印a，线程2打印b，线程3打印c，实现abcabc...(5次）
     * 单个condition + while(条件)实现（类似wait、notify）
     */
    @Test
    public void testWaitNotifySolution() {
        new Thread(() -> waitNotifySolution(0, "a", 1), "t1").start();

        new Thread(() -> waitNotifySolution(1, "b", 2), "t2").start();

        new Thread(() -> waitNotifySolution(2, "c", 0), "t3").start();

        ThreadUtils.sleep(3000);
    }

    private void waitNotifySolution(int flag, String print, int nextFlag) {
        for (int i = 0; i < 5; i++) {
            lock.lock();
            try {
                while (status != flag) {
                    try {
                        condition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.info(print);
                status = nextFlag;
                condition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }


    private Condition aReady = lock.newCondition();
    private Condition bReady = lock.newCondition();
    private Condition cReady = lock.newCondition();

    /**
     * 线程1打印a，线程2打印b，线程3打印c，实现abcabc...(5次）
     * 多个condition实现
     */
    @Test
    public void testMultiConditionSolution() {
        new Thread(() -> {
            multiConditionSolution(aReady, "a", bReady);
        }, "t1").start();

        new Thread(() -> {
            multiConditionSolution(bReady, "b", cReady);
        }, "t2").start();

        new Thread(() -> {
            multiConditionSolution(cReady, "c", aReady);
        }, "t3").start();

        ThreadUtils.sleep(500);
        lock.lock();
        try {
            aReady.signalAll();
        } finally {
            lock.unlock();
        }
        ThreadUtils.sleep(3000);
    }

    /**
     *
     * @param condition
     * @param print
     * @param nextCondition
     */
    private void multiConditionSolution(Condition condition, String print, Condition nextCondition) {
        for (int i = 0; i < 5; i++) {
            lock.lock();
            try {
                try {
                    condition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.info(print);
                nextCondition.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    Thread t1, t2, t3;

    /**
     * 线程1打印a，线程2打印b，线程3打印c，实现abcabc...(5次）
     * park实现
     */
    @Test
    public void testParkSolution() {

        t1 = new Thread(() -> {
            parkSolution( "a", t2);
        }, "t1");
        t1.start();

        t2 = new Thread(() -> {
            parkSolution( "b", t3);
        }, "t2");
        t2.start();

        t3 = new Thread(() -> {
            parkSolution( "c", t1);
        }, "t3");
        t3.start();

        LockSupport.unpark(t1);
        ThreadUtils.sleep(3000);
    }

 
    private void parkSolution(String print, Thread nextThread) {
        for (int i = 0; i < 5; i++) {
            LockSupport.park();
            log.info(print);
            LockSupport.unpark(nextThread);
        }
    }

    private static final Object sLock = new Object();
    private static boolean t2NotOver = true;

    /**
     * 先打印 2，再打印1, park实现
     *
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
