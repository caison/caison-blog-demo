package org.caison.disruptor.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * disruptor消息事件
 * @author ChenCaihua
 * @date 2019年04月25日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LongEvent {
    private long value;
}
