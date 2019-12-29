package org.caison.netty.demo.memory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;

/**
 * @author ChenCaihua
 * @date 2019年12月23日
 */
public class BufferLeaksDemo {
    // -Dio.netty.leakDetectionLevel=paranoid
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 500000; ++i) {
            ByteBuf byteBuf = UnpooledByteBufAllocator.DEFAULT.buffer(1024);
            byteBuf.release();
            byteBuf = null;

        }
        System.gc();
        // Thread.sleep(1000);
    }
}
