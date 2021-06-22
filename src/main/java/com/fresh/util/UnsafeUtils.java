package com.fresh.util;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author guowenyu
 * @since 2021/5/13
 */
public class UnsafeUtils {

    private static Unsafe unsafe;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Unsafe getUnsafe() {
        return unsafe;
    }

}
