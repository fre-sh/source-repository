package com.fresh.juc.keyword;

import org.openjdk.jol.info.ClassLayout;

/**
 * @author guowenyu
 * @since 2021/6/2
 */
public class BiasedLockDemo {

    public static void main(String[] args) throws InterruptedException {
        OB o = new OB();
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        // 重写hashcode 不调用Object.hashcode ，就不会撤销偏向状态
        o.hashCode();
        synchronized (o) {
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }

}
class OB{

    @Override
    public int hashCode() {
        return 0;
    }
}