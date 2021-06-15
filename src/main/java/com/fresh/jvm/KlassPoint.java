package com.fresh.jvm;

import com.fresh.unsafe.UnsafeUtils;
import org.openjdk.jol.info.ClassLayout;

/**
 * @author guowenyu
 * @since 2021/6/7
 */
public class KlassPoint {

    private static final String constStr = "hello";
    private static final int constInt = 1;

    public static void main(String[] args) {
        Object o = new Object();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());
        System.out.println(ClassLayout.parseInstance(o.getClass()).toPrintable());
//        UnsafeUtils.getUnsafe().getAddress()
        Object a = 1;
    }

}
