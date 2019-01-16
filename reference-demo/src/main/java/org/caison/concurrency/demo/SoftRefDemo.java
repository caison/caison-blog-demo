package org.caison.concurrency.demo;

import java.lang.ref.SoftReference;

/**
 * 功能：软引用demo
 * 详情：
 * @author ChenCaihua
 * @since 2018年09月27日
 */
public class SoftRefDemo {
    public static void main(String[] args) {
        SoftReference<String> sr = new SoftReference<>( new String("hello world "));
        System.out.println(sr.get());
    }
}
