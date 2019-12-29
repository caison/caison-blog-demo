package org.caison.netty.demo.monitor.overstock.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.SingleThreadEventExecutor;

import java.lang.reflect.Field;
import java.util.Queue;

/**
 * @author ChenCaihua
 * @date 2019年12月25日
 */
public class MonitorHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws NoSuchFieldException, IllegalAccessException {
        monitorPendingTaskCount(ctx);
        monitorQueueFirstTask(ctx);
        monitorOutboundBufSize(ctx);
    }

    /** 监控任务队列堆积任务数，任务队列中的任务包括io读写任务，业务程序提交任务 */
    public void monitorPendingTaskCount(ChannelHandlerContext ctx) {
        int totalPendingSize = 0;
        for (EventExecutor eventExecutor : ctx.executor().parent()) {
            SingleThreadEventExecutor executor = (SingleThreadEventExecutor) eventExecutor;
            // 注意，Netty4.1.29以下版本本pendingTasks()方法存在bug，导致线程阻塞问题
            // 参考 https://github.com/netty/netty/issues/8196
            totalPendingSize += executor.pendingTasks();
        }
        System.out.println("任务队列中总任务数 = " + totalPendingSize);
    }

    /** 监控各个堆积的任务队列中第一个任务的类信息 */
    public void monitorQueueFirstTask(ChannelHandlerContext ctx) throws NoSuchFieldException, IllegalAccessException {
        Field singleThreadField = SingleThreadEventExecutor.class.getDeclaredField("taskQueue");
        singleThreadField.setAccessible(true);
        for (EventExecutor eventExecutor : ctx.executor().parent()) {
            SingleThreadEventExecutor executor = (SingleThreadEventExecutor) eventExecutor;
            Runnable task = ((Queue<Runnable>) singleThreadField.get(executor)).peek();
            if (null != task) {
                System.out.println("任务队列中第一个任务信息：" + task.getClass().getName());
            }
        }
    }

    /** 监控出站消息的队列积压的byteBuf大小 */
    public void monitorOutboundBufSize(ChannelHandlerContext ctx) {
        long outBoundBufSize = ((NioSocketChannel) ctx.channel()).unsafe().outboundBuffer().totalPendingWriteBytes();
        System.out.println("出站消息队列中积压的buf大小" + outBoundBufSize);
    }

}
