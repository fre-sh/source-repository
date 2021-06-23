package com.fresh.jvm;

import lombok.extern.slf4j.Slf4j;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * @author guowenyu
 * @since 2021/6/23
 */
@Slf4j
public class TestReference {

    public static void main(String[] args) {

        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        Object o = new byte[1024 * 1024];
        PhantomReference<Object> phantomReference = new PhantomReference<>(o, queue);
        o = new Object();

        log.info("{}", queue.poll());

        System.gc();

        log.info("{}", queue.poll());
    }

}
