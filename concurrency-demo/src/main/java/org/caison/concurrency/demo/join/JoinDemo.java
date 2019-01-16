package org.caison.concurrency.demo.join;

/**
 * 功能：
 * 详情：
 * @author ChenCaihua
 * @since 2019年01月13日
 */

public class JoinDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            try {
                System.out.println("<A线程> A 任务开始");
                Thread.sleep(1000);
                System.out.println("<A线程> A 任务完成");
            } catch (InterruptedException ignored) {
            }
        });

        Thread threadB = new Thread(() -> {
            try {
                System.out.println("<B线程> B 任务开始");
                Thread.sleep(1000);
                System.out.println("<B线程> B 任务完成");
            } catch (InterruptedException ignored) {
            }
        });

        threadA.start();
        threadA.join();
        System.out.println("<主线程> A join执行完毕");

        threadB.start();
        threadB.join();
        System.out.println("<主线程> B join执行完毕");

        /* 输出：
            <A线程> A 任务开始
            <A线程> A 任务完成
            <主线程> A join执行完毕
            <B线程> B 任务开始
            <B线程> B 任务完成
            <主线程> B join执行完毕
        */
    }
}
