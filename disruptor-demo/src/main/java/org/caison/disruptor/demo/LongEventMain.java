package org.caison.disruptor.demo;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executors;

/**
 * @author ChenCaihua
 * @date 2019年04月25日
 */
public class LongEventMain {

    public static void main(String[] args) throws Exception {

        Disruptor<LongEvent> disruptor = initDisruptor(1);

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventProducer producer = new LongEventProducer(ringBuffer);
        long eventCount = 1000L;
        for (long i = 0; i <= eventCount; ++i) {
            producer.onData(i);
        }

    }

    /**
     * 初始化disruptor
     * @param threadNum 线程数
     * @return 处理结果
     */
    private static Disruptor<LongEvent> initDisruptor(int threadNum) {
        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(LongEvent::new, bufferSize, Executors.newFixedThreadPool(threadNum));

        // Connect the handler
        disruptor.handleEventsWith(new LongEventHandler());

        // Start the Disruptor, starts all threads running
        disruptor.start();

        return disruptor;
    }

}