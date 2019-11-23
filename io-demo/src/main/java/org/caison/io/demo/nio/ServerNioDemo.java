package org.caison.io.demo.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * 功能：
 * 详情：
 *
 * @author ChenCaihua
 * @since 2019年11月10日
 */
public class ServerNioDemo {

    public static void main(String[] args) throws IOException {

        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(9091));
        // 配置通道为非阻塞模式
        serverSocketChannel.configureBlocking(false);
        // 注册服务端的socket-accept事件
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            // selector.select()会一直阻塞，直到有channel相关操作就绪
            selector.select();
            // SelectionKey关联的channel都有就绪事件
            Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();

            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();

                // 服务端socket-accept
                if (key.isAcceptable()) {
                    // 获取客户端连接的channel
                    SocketChannel clientSocketChannel = serverSocketChannel.accept();
                    // 设置为非阻塞模式
                    clientSocketChannel.configureBlocking(false);
                    // 注册监听该客户端channel可读事件，并为channel关联新分配的buffer
                    clientSocketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocateDirect(1024));
                }

                // channel可读
                if (key.isReadable()) {

                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer buf = (ByteBuffer) key.attachment();

                    int bytesRead;
                    StringBuilder reqMsg = new StringBuilder();
                    while ((bytesRead = socketChannel.read(buf)) > 0) {
                        // 从buf写模式切换为读模式
                        buf.flip();
                        int bufRemain = buf.remaining();
                        byte[] bytes = new byte[bufRemain];
                        buf.get(bytes, 0, bytesRead);
                        // 这里当数据包大于byteBuffer长度，有可能有粘包/拆包问题
                        reqMsg.append(new String(bytes, StandardCharsets.UTF_8));
                        buf.clear();
                    }
                    System.out.println("服务端收到报文：" + reqMsg.toString());

                    if (bytesRead == -1) {
                        byte[] bytes = "[这是服务回的报文的报文]".getBytes(StandardCharsets.UTF_8);

                        int length;
                        for (int offset = 0; offset < bytes.length; offset += length) {
                            length = Math.min(buf.capacity(), bytes.length - offset);
                            buf.clear();
                            buf.put(bytes, offset, length);
                            buf.flip();
                            socketChannel.write(buf);
                        }
                        socketChannel.close();
                    }

                }
                // Selector不会自己从已selectedKeys中移除SelectionKey实例
                // 必须在处理完通道时自己移除 下次该channel变成就绪时，Selector会再次将其放入selectedKeys中
                keyIterator.remove();
            }
        }
    }
}
