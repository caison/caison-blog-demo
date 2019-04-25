package org.caison.disruptor.demo;

import com.lmax.disruptor.EventHandler;

/**
 * 定义一个事件处理类，用户具体处理事件的实现
 *
 * @author ChenCaihua
 * @date 2019年04月25日
 */
public class LongEventHandler implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {

        System.out.println("Event: " + event);
    }
}
