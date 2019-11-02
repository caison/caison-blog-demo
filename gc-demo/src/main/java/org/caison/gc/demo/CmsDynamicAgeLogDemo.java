package org.caison.gc.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * java动态年龄计算demo
 * java版本jdk1.8.0_201
 * <p>
 * 空间分配 eden=100m, s0=50m, s1=50m, old=100m
 * 启动参数
 * -Xms300m -Xmx300m -Xmn200m -XX:SurvivorRatio=2 -XX:+UseConcMarkSweepGC -XX:+PrintGC -XX:+PrintGCDetails
 * -XX:+PrintGCDateStamps  -XX:+PrintHeapAtGC -Xloggc:./gc-demo/cms-dynamic-age-gc.log
 *
 * @author ChenCaihua
 * @date 2019年08月29日
 */
public class CmsDynamicAgeLogDemo {
    private static final int Mb = 1024 * 1024;

    public static void main(String[] args) throws InterruptedException {
         byte[] b1 = new byte[(int) (20 * Mb)];

        for (int i = 0; i < 20; ++i) {
            byte[] b = new byte[25 * Mb];
            Thread.sleep(1000);
            System.out.println("current time = " + getCurrentTime());
        }
        Thread.sleep(5000);
    }

    private static String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

}
