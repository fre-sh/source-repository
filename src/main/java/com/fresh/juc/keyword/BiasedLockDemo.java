package com.fresh.juc.keyword;

import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;

/**
 * @author guowenyu
 * @since 2021/6/2
 */
public class BiasedLockDemo {

    public static void main(String[] args) throws InterruptedException {
        reBiasedOneObj();
    }

    private static void contendCancelBiased() throws InterruptedException {
        Object o = new Object();

        new Thread(() -> {
            synchronized (o) {
                System.out.println("[thread 1] get lock ----------");
                System.out.println(getMarkWord(o));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("[thread 1] get lock 2----------");
                System.out.println(getMarkWord(o));
            }
            System.out.println("[thread 1] free lock ----------");
        }, "thread1").start();

        TimeUnit.MICROSECONDS.sleep(200);
        new Thread(() -> {
            System.out.println("[thread 2] try lock ----------");
            synchronized (o) {
                System.out.println("[thread 2] get lock ----------");
                System.out.println(getMarkWord(o));
            }
            System.out.println("[thread 2] free lock ----------");
            System.out.println(getMarkWord(o));

        }, "thread2").start();
    }

    static Thread thread2;

    private static void reBiasedOneObj() throws InterruptedException {
        List<Object> list = new ArrayList<>();
        Object o = new Object();

        thread2 = new Thread(() -> {
            for (int i = 1; i <= 25; i++) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (o) {
                    System.out.println("[thread 2] get lock " + i + "----------");
                    System.out.println(getMarkWord(o));
                }
                System.out.println("[thread 2] free lock " + i + "----------");
                System.out.println(getMarkWord(o));
            }
        }, "thread2");
        thread2.start();

        for (int i = 1; i <= 25; i++) {
            list.add(o);
            synchronized (o) {
                System.out.println("[thread 1] get lock " + i + "----------");
                System.out.println(getMarkWord(o));
            }
            System.out.println("[thread 1] free lock " + i + "----------");
            Thread.sleep(500);
        }



    }

    private static void reBiased() throws InterruptedException {
        List<Object> list = new ArrayList<>();
        for (int i = 1; i <= 25; i++) {
//        for (int i = 1; i <= 10; i++) {
//            Object o = i< 10 ? new Object() : new Integer(i);
            Object o = new Object();
            list.add(o);
            synchronized (o) {
                System.out.println("[thread 1] get lock " + i + "----------");
                System.out.println(getMarkWord(o));
            }
            System.out.println("[thread 1] free lock " + i + "----------");
        }

//        new Thread(() -> {
//            for (int i = 11; i <= 25; i++) {
////            Object o = i< 10 ? new Object() : new Integer(i);
//                Object o = new Object();
//                list.add(o);
//                synchronized (o) {
//                    System.out.println("[thread 3] get lock " + i + "----------");
//                    System.out.println(getMarkWord(o));
//                }
//                System.out.println("[thread 3] free lock " + i + "----------");
//            }
//            LockSupport.unpark(thread2);
//        }).start();

        thread2 = new Thread(() -> {
//            LockSupport.park();
            for (int i = 1; i <= 25; i++) {
                Object o = list.get((i - 1) % 25);
                synchronized (o) {
                    System.out.println("[thread 2] get lock " + i + "----------");
                    System.out.println(getMarkWord(o));
                }
                System.out.println("[thread 2] free lock " + i + "----------");
                System.out.println(getMarkWord(o));
            }
        }, "thread2");
        thread2.start();


//        new Thread(() -> {
//            for (int i = 10; i <= 25; i++) {
//                Object o = list.get(i-1);
//                synchronized (o) {
//                    System.out.println("[thread 3] get lock " + i + "----------");
//                    System.out.println(getMarkWord(o));
//                }
//                System.out.println("[thread 3] free lock " + i + "----------");
//                System.out.println(getMarkWord(o));
//            }
//        }, "thread3").start();
    }

    private static void hashcodeCancelBiased() {
        OB o = new OB();
        System.out.println(getMarkWord(o));

        // 重写hashcode 不调用Object.hashcode ，就不会撤销偏向状态
        o.hashCode();
        synchronized (o) {
            System.out.println(getMarkWord(o));
        }
    }

    private static String getMarkWord(Object o) {
        ClassLayout classLayout = ClassLayout.parseInstance(o);
        String str = classLayout.toPrintable();
        int begin = str.indexOf("0x");
        int end = str.indexOf(")", begin);
        return str.substring(begin, end + 1);
    }
}

class OB {
    public int f;

    private void f() {
        System.out.println(1);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

class A extends OB {
    public int f;

    private void f() {
        System.out.println(2);
    }
}