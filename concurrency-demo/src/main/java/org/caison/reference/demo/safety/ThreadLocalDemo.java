package org.caison.reference.demo.safety;

/**
 * 功能：
 * 详情：
 * @author ChenCaihua
 * @since 2019年01月18日
 */
public class ThreadLocalDemo {
    public static void main(String[] args) {


        ThreadLocal<String> threadLocal1 = new ThreadLocal<>();
        ThreadLocal<String> threadLocal2 = new ThreadLocal<>();

        Thread threadA = new Thread(() -> {
            threadLocal1.set("localVal1");
            threadLocal2.set("localVal2");
        });
        Thread threadB = new Thread(() -> {
            threadLocal1.set("localVal1");
            threadLocal2.set("localVal2");
        });

        threadA.start();
        threadB.start();



    }
}
