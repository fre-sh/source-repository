package com.fresh.juc.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author guowenyu
 * @since 2021/5/13
 */
public class CasDemo {

    static AtomicInteger integer = new AtomicInteger();

    public static void main(String[] args) {
        int i;
        do {
            i = integer.get();
        } while (!integer.compareAndSet(i, i + 1));
    }

}
