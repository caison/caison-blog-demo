package org.caison.disruptor.demo;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

/**
 * 用户实现生产数据（事件）的类
 *
 * @author ChenCaihua
 * @date 2019年04月25日
 */
public class LongEventProducer {
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(long inputData) {
        ringBuffer.publishEvent((event, sequence, longMsg) -> event.setValue(longMsg), inputData);
    }
}
