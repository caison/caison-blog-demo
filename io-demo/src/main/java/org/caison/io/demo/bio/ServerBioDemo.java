package org.caison.io.demo.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 功能：
 * 详情：
 * @author ChenCaihua
 * @since 2019年11月10日
 */
public class ServerBioDemo {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket(9091);
        while (true) {
            // 等待连接
            Socket socket = serverSocket.accept();

            Thread handlerThread = new Thread(() -> {

                // 读取请求消息
                StringBuilder req = new StringBuilder();
                byte[] recvByteBuf = new byte[1024];
                int len;

                try {
                    while ((len = socket.getInputStream().read(recvByteBuf)) != -1) {
                        req.append(new String(recvByteBuf, 0, len, StandardCharsets.UTF_8));
                    }
                    System.out.println(
                            System.currentTimeMillis() + " client request message: " + req.toString());

                    // 写入返回消息
                    socket.getOutputStream().write(("server response msg".getBytes()));
                    socket.shutdownOutput();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // 业务处理
            handlerThread.start();
        }
    }
}



