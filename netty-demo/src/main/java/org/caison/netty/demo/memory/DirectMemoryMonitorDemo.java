package org.caison.netty.demo.memory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.util.internal.PlatformDependent;
import sun.management.ManagementFactoryHelper;
import sun.nio.ch.DirectBuffer;

import java.lang.management.BufferPoolMXBean;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * VisualVM堆外内存排查监控demo
 *
 * @author ChenCaihua
 * @date 2019年12月20日
 */
public class DirectMemoryMonitorDemo {

    private static final int CHUNK_SIZE = 16 * 1024 * 1024;
    private static final int PAGE_SIZE = CHUNK_SIZE / 2048;
    private static final int ONE_MB = 1024 * 1024;

    private static final int sleepWhenStartAndStop = 150;
    private static final int sleepWhenRun = 20;

    private static List<ByteBuf> byteBufList = new ArrayList<>();
    private static List<ByteBuffer> byteBufferList = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        Thread.sleep(sleepWhenStartAndStop);
        monitorByteBuffer();

        Thread.sleep(sleepWhenRun);
        // -Dio.netty.maxDirectMemory=0
        monitorByteBuf();
        Thread.sleep(sleepWhenStartAndStop);
    }


    /**
     * 监控java.nio的ByteBuf堆外内存
     * 默认使用noCleaner策略
     * 设置-Dio.netty.maxDirectMemory=0会改为使用hasCleaner策略
     *
     * @throws InterruptedException
     */
    private static void monitorByteBuf() throws InterruptedException {

        for (int i = 0; i < 5; ++i) {
            byteBufList.add(UnpooledByteBufAllocator.DEFAULT.buffer(ONE_MB));
            Thread.sleep(sleepWhenRun);
            System.out.println("[noCleaner分配] 堆外内存占用 " +   PlatformDependent.usedDirectMemory());
        }
        System.out.println();
        for (ByteBuf byteBuf : byteBufList) {
            Thread.sleep(sleepWhenRun);
            byteBuf.release();

            System.out.println("[noCleaner释放] 堆外内存占用 " +   PlatformDependent.usedDirectMemory());
        }
    }


    /**
     * 监控java.nio的ByteBuffer堆外内存
     * 设置-XX:MaxDirectMemorySize=6m，则限定hasCleaner堆外内存最多占用6m
     *
     * @throws InterruptedException
     */
    private static void monitorByteBuffer() throws Exception {

        List<BufferPoolMXBean> bufferPoolMXBeans = ManagementFactoryHelper.getBufferPoolMXBeans();
        BufferPoolMXBean directBufferMXBean = bufferPoolMXBeans.get(0);


        for (int i = 0; i < 5; ++i) {
            byteBufferList.add(ByteBuffer.allocateDirect(ONE_MB));
            Thread.sleep(sleepWhenRun);
            System.out.println("[hasCleaner分配] 堆外内存占用 " + directBufferMXBean.getMemoryUsed());
        }

        System.out.println();

        for (ByteBuffer byteBuffer : byteBufferList) {
            Thread.sleep(sleepWhenRun);
            PlatformDependent.freeDirectBuffer(byteBuffer);
            ((DirectBuffer) byteBuffer).cleaner().clean();
            System.out.println("[hasCleaner释放] 堆外内存占用 " + directBufferMXBean.getMemoryUsed());
        }
        System.out.println();
    }
}
