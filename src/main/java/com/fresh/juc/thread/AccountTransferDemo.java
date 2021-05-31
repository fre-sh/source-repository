package com.fresh.juc.thread;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

/**
 * @author guowenyu
 * @since 2021/5/20
 */
public class AccountTransferDemo {


    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(10);
        Random random = new Random();
        Account a = new Account(1000);
        Account b = new Account(1000);

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    a.transfer(b, random.nextInt(50));
                    b.transfer(a, random.nextInt(50));
                }
                countDownLatch.countDown();
            }).start();
        }

        countDownLatch.await();
        System.out.println(a.getMoney());
        System.out.println(b.getMoney());
        System.out.println(a.getMoney() + b.getMoney());
    }

}

class Account {
    private int money;

    public void transfer(Account other, int amount) {
        synchronized (Account.class) {
//        synchronized (this) {
            if (money > amount) {
                money -= amount;
                other.money += amount;
            }
        }
    }

    public Account(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }
}
