package org.caison.concurrency.demo.lock;

/**
 * 功能：
 * 详情：
 * @author ChenCaihua
 * @since 2019年01月13日
 */
public class DeadLockDemo {
    public static void main(String[] args) {
        Object lockA = new Object();
        Object lockB = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                try {
                    Thread.sleep(1001);
                } catch (InterruptedException ignored) { }

                synchronized (lockB) { }
            }
        });
        Thread t2 = new Thread(() -> {
            synchronized (lockB) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) { }
                synchronized (lockA) { }
            }
        });

        t1.start();
        t2.start();
    }
}
