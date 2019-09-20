package org.caison.gc.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * java版本jdk1.8.0_201
 * <p>
 * 启动参数
  -Xms100m -Xmx100m -XX:+UseG1GC -XX:+PrintGC -XX:+PrintGCDetails
  -XX:+PrintGCDateStamps  -XX:+PrintHeapAtGC -Xloggc:./gc-demo/g1-gc.log
 *
 * @author ChenCaihua
 * @date 2019年08月29日
 */
public class G1LogDemo {
    private static final int Mb = 1024 * 1024;

    public static void main(String[] args) throws InterruptedException {

        // eden
        System.out.println(getCurrentTime() + " before allocation1 ");
        byte[] allocation = new byte[32 * Mb];
        System.out.println(getCurrentTime() + " after allocation1 \n");
        Thread.sleep(2000);

        // 触发minor gc
        System.out.println(getCurrentTime() + " before allocation2 ");
        allocation = new byte[3 * Mb];
        System.out.println(getCurrentTime() + " after allocation2 \n");
        Thread.sleep(2000);

        byte[][] allByte = new byte[10][];

        // old gc
        for (int i = 0; i < 10; ++i) {
            System.out.println(getCurrentTime() + " before list allocation, index = " + i);
            allByte[i] = new byte[3 * Mb];

            System.out.println(getCurrentTime() + " after list allocation, index = " + i + "\n");
            Thread.sleep(2000);
        }
        Thread.sleep(3000);
    }

    private static String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

}
