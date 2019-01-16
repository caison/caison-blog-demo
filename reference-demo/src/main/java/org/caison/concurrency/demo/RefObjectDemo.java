package org.caison.concurrency.demo;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能：引用对象demo
 * 详情：
 * @author ChenCaihua
 * @since 2018年09月27日
 */
public class RefObjectDemo {

    /**
     * 简单使用demo
     */
    private static void simpleUseDemo(){
        List<String> myList = new ArrayList<>();
        SoftReference<List<String>> refObj = new SoftReference<>(myList);

        List<String> list = refObj.get();
        if (null != list) {
            list.add("hello");
        } else {
            // 整个列表已经被垃圾回收了，做其他处理
        }
    }

    /**
     * 正确使用demo
     */
    private static void trueUseRefObjDemo(){
        List<String> myList = new ArrayList<>();
        SoftReference<List<String>> refObj = new SoftReference<>(myList);

        // 正确的使用，使用强引用指向对象保证获得对象之后不会被回收
        List<String> list = refObj.get();
        if (null != list) {
            list.add("hello");
        } else {
            // 整个列表已经被垃圾回收了，做其他处理
        }
    }

    /**
     * 错误使用demo
     */
    private static void falseUseRefObjDemo(){
        List<String> myList = new ArrayList<>();
        SoftReference<List<String>> refObj = new SoftReference<>(myList);

        // XXX 错误的使用，在检查对象非空到使用对象期间，对象可能已经被回收
        // 可能出现空指针异常
        if (null != refObj.get()) {
            refObj.get().add("hello");
        }
    }

    public static void main(String[] args) {
        simpleUseDemo();
        trueUseRefObjDemo();
        falseUseRefObjDemo();
    }

}
