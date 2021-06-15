package com.fresh.classloader;

/**
 * @author guowenyu
 * @since 2021/5/31
 */
public class CustomClassLoader extends ClassLoader {

    public static void main(String[] args) throws ClassNotFoundException {
        CustomClassLoader customClassLoader = new CustomClassLoader();
        ClassLoader parent = customClassLoader.getParent();
        System.out.println(parent);


        Class<?> aClass = customClassLoader.loadClass("com.fresh.classloader.CustomClassLoader.A", true);

    }

    static class A {
        public void run(){
            System.out.println("A");
        }
    }

}