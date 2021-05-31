package com.fresh.juc.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.LockSupport;

/**
 * @author guowenyu
 * @since 2021/5/19
 */
//@Slf4j
public class ThreadDemo {
    static Logger log = LoggerFactory.getLogger(ThreadDemo.class);

    public static void main(String[] args) {
        final Thread thread = new Thread(() -> {
            System.out.println("sleep");
            LockSupport.parkUntil(2000);
            log.info("end ");
        });

        thread.start();
        thread.interrupt();

        LockSupport.parkUntil(1000);
        System.out.println(thread.isInterrupted());
        Thread.activeCount();
    }

}
