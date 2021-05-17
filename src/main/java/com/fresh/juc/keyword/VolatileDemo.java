package com.fresh.juc.keyword;

import org.junit.Test;

import java.sql.DriverManager;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author guowenyu
 * @since 2021/5/12
 */
public class VolatileDemo {

    @Test
    public void testAtomic() {
        MyData myData = new MyData();
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    myData.increment();
                }
            }, "" + i).start();
        }

        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        // 如果保证原子性，最终=20000
        System.out.println("final num = " + myData.num);
        System.out.println("final num = " + myData.atomicNum);

    }

    static class MyData {
        AtomicInteger atomicNum = new AtomicInteger(0);
        int num = 0;
        int x;

        public void increment() {
            num++; // 相当于 num = num + 1
            atomicNum.addAndGet(1);
//            x = num ++;
        }
    }

}
