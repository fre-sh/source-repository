package com.fresh.util;

/**
 * @author guowenyu
 * @since 2021/6/15
 */
public class ThreadUtils {

    public static void sleep(long ms){
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
