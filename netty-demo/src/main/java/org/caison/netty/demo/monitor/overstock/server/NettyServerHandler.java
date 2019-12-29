package org.caison.netty.demo.monitor.overstock.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * @author ChenCaihua
 * @date 2019年12月24日
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println(new Date() + ": 服务端读到数据 -> " + byteBuf.toString(StandardCharsets.UTF_8));
        byteBuf.release();

        // 回复数据到客户端
        System.out.println(new Date() + ": 服务端写出数据");

        byte[] bytes = "你好，欢迎关注我的公众号，《分布式系统架构》!".getBytes(StandardCharsets.UTF_8);
        ByteBuf bufferOut = ctx.alloc().directBuffer();
        bufferOut.writeBytes(bytes);

        System.out.println("before write refCnt = " + bufferOut.refCnt());
        ctx.channel().writeAndFlush(bufferOut).addListener(future->
                System.out.println("after write refCnt = " + bufferOut.refCnt()));
        // System.out.println("数据写出成功");

    }
}