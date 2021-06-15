package com.fresh.nio;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author guowenyu
 * @since 2021/6/8
 */
public class PathDemo {

    @Test
    public void testP() {
        Path path = Paths.get("E:\\tmp\\hiveproxy日志\\work");
        path.forEach(System.out::println);
        Path child = path.resolve("krb5.conf");
        System.out.println(child.toString());

        new FutureTask<>(() -> {
        }, null);
    }

    @Test
    public void testDirectoryStream() throws IOException {
        Path path = Paths.get("E:\\tmp\\hiveproxy日志\\lk");

        if (Files.exists(path)) {
            for (Path p : Files.newDirectoryStream(path)) {
                System.out.println(p);
            }
        }
    }

    static Lock lock = new ReentrantLock();

    public static void main(String[] args) {

    }
}
