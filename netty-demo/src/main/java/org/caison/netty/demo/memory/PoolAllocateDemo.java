package org.caison.netty.demo.memory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;

/**
 * @author ChenCaihua
 * @date 2019年12月09日
 */
public class PoolAllocateDemo {

    public static void main(String[] args) {
        int chunkSize = 16 * 1024 * 1024;
        int pageSize = chunkSize / 2048;

        ByteBuf buffer = PooledByteBufAllocator.DEFAULT.buffer(pageSize * 2);

        buffer.release();
    }
}
