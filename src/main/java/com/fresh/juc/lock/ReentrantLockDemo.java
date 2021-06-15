package com.fresh.juc.lock;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author guowenyu
 * @since 2021/6/9
 */
@Slf4j
public class ReentrantLockDemo {

    Lock lock = new ReentrantLock();

    @Test
    public void testInterrupt(){
        new Thread(() -> {
            lock.lock();
        }, "").start();
    }

}
