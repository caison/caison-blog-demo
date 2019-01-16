package org.caison.concurrency.demo;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * 功能：弱引用demo
 * 详情：
 * @author ChenCaihua
 * @since 2018年09月27日
 */
public class WeakRefDemo {

    /**
     * 引用队列demo
     */
    private static void refQueueDemo() {
        ReferenceQueue<String> refQueue = new ReferenceQueue<>();

        // 用于检查引用队列中的引用值被回收
        Thread checkRefQueueThread = new Thread(() -> {
            while (true) {
                Reference<? extends String> clearRef = refQueue.poll();
                if (null != clearRef) {
                    System.out
                            .println("引用对象被回收, ref = " + clearRef + ", value = " + clearRef.get());
                }
            }
        });
        checkRefQueueThread.start();

        WeakReference<String> weakRef1 = new WeakReference<>(new String("value1"), refQueue);
        WeakReference<String> weakRef2 = new WeakReference<>(new String("value2"), refQueue);
        WeakReference<String> weakRef3 = new WeakReference<>(new String("value3"), refQueue);

        System.out.println("ref1 value = " + weakRef1.get() + ", ref2 value = " + weakRef2.get()
                + ", ref3 value = " + weakRef3.get());

        System.out.println("开始通知JVM的gc进行垃圾回收");
        // 通知JVM的gc进行垃圾回收
        System.gc();
    }

    /**
     *  weakHashMap使用demo
     */
    private static void weakHashMapDemo() {
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<>();
        String key1 = new String("key1");
        String key2 = new String("key2");
        String key3 = new String("key3");
        weakHashMap.put(key1, "value1");
        weakHashMap.put(key2, "value2");
        weakHashMap.put(key3, "value3");

        // 使没有任何强引用指向key1
        key1 = null;

        System.out.println(
                "before: gc weakHashMap = " + weakHashMap + " , size=" + weakHashMap.size());

        // 通知JVM的gc进行垃圾回收
        System.gc();
        System.out.println(
                "after: gc weakHashMap = " + weakHashMap + " , size=" + weakHashMap.size());
    }

    /**
     * 简单使用弱引用demo
     */
    private static void simpleUseWeakRefDemo() {
        WeakReference<String> sr = new WeakReference<>(new String("hello world "));
        System.out.println("before gc -> " + sr.get());

        // 通知JVM的gc进行垃圾回收
        System.gc();
        System.out.println("after gc -> " + sr.get());
    }

    public static void main(String[] args) {
        // simpleUseWeakRefDemo();
        // weakHashMapDemo();
        refQueueDemo();
    }
}
