package org.caison.reference.demo.safety;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 功能：
 * 详情：
 * @author ChenCaihua
 * @since 2019年01月17日
 */
public class CasDemo {
    private volatile int noCasCount = 0;
    private volatile int casCount = 0;
    private static Unsafe UNSAFE;

    /** casCount在CasDemo的内存偏移量*/
    private static final long casCountOffset;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            UNSAFE = (Unsafe) field.get(null);
        } catch (Exception ignore) {
        }
        assert UNSAFE != null;
        try {
            casCountOffset = UNSAFE.objectFieldOffset(CasDemo.class.getDeclaredField("casCount"));
        } catch (Exception ex) {
            throw new Error(ex);
        }
    }

    class NoCasThread extends Thread {

        @Override
        public void run() {
            for (int i = 0; i < 10000; ++i) {
                ++noCasCount;
            }
        }
    }

    class CasThread extends Thread {

        @Override
        public void run() {
            for (int i = 0; i < 10000; ++i) {
                casIncrOne();
            }
        }

        private void casIncrOne() {
            int nowCount;
            do {
                nowCount = casCount;
            } while (!UNSAFE
                    .compareAndSwapInt(CasDemo.this, casCountOffset, nowCount, nowCount + 1));
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CasDemo casDemo = new CasDemo();
        for (int i = 0; i < 10; ++i) {
            NoCasThread noCasThread = casDemo.new NoCasThread();
            noCasThread.start();
            CasThread casThread = casDemo.new CasThread();
            casThread.start();
        }

        Thread.sleep(1000);
        System.out.println("noCasCount = " + casDemo.noCasCount);
        System.out.println("casCount = " + casDemo.casCount);

    }
}
