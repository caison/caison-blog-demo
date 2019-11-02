package org.caison.concurrency;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ChenCaihua
 * @date 2019年09月30日
 */
public class AqsDemo {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        });

        Thread t2 = new Thread(() -> {
            lock.lock();
            try {
                Thread.sleep(5 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        });

        t1.start();
        Thread.sleep(500);
        t2.start();
        t2.join();
    }
}
