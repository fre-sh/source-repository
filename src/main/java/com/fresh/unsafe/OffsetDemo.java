package com.fresh.unsafe;

import com.fresh.util.UnsafeUtils;
import sun.misc.Unsafe;

/**
 * @author guowenyu
 * @since 2021/5/13
 */
public class OffsetDemo {

    public static void main(String[] args) throws Exception {
        Unsafe unsafe = UnsafeUtils.getUnsafe();

        AUser aUser = new AUser();
        ACar aCar = new ACar();

        System.out.println(unsafe.objectFieldOffset(AUser.class.getDeclaredField("id")));
        System.out.println(unsafe.objectFieldOffset(AUser.class.getDeclaredField("name")));
        System.out.println(unsafe.objectFieldOffset(ACar.class.getDeclaredField("id")));
        System.out.println(unsafe.objectFieldOffset(ACar.class.getDeclaredField("name")));
    }

    static class AUser{
        private int id;
        private String name;
    }

    static class ACar{
        private int id;
        private String name;
    }
}
