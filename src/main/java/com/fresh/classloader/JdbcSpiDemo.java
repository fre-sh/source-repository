package com.fresh.classloader;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author guowenyu
 * @since 2021/6/11
 */
public class JdbcSpiDemo {

    public static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static void main(String[] args) throws Exception {
//        Class.forName(DRIVER);
//        Class<?> aClass = ClassLoader.getSystemClassLoader().loadClass(DRIVER);
//        Object o = aClass.newInstance();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306", "root", "root")) {
            System.out.println("connected");
        }
    }

}