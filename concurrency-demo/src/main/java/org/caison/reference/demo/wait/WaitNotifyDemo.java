package org.caison.reference.demo.wait;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 功能：
 * 详情：
 * @author ChenCaihua
 * @since 2019年01月13日
 */
public class WaitNotifyDemo {

    private synchronized void forNotify() {
        System.out.println("for notify begin");
        notify();
        System.out.println("for notify finish");
    }

    private synchronized void forWaitA() {
        System.out.println("for wait A begin");
        try {
            wait();
        } catch (InterruptedException ignored) {
        }
        System.out.println("for wait A finish");
    }

    private synchronized void forWaitB() {
        System.out.println("for wait B begin");
        try {
            wait();
        } catch (InterruptedException ignored) {
        }
        System.out.println("for wait B finish");
    }

    public static void main(String[] args) {
        WaitNotifyDemo waitNotifyDemo = new WaitNotifyDemo();

        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(waitNotifyDemo::forWaitA);
        executorService.execute(waitNotifyDemo::forWaitB);
        executorService.execute(waitNotifyDemo::forNotify);
        /* 输出：
        for wait A begin
        for wait B begin
        for notify begin
        for notify finish
        for wait A finish
         */
    }

}
