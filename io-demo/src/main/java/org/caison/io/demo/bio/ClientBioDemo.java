package org.caison.io.demo.bio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * 功能：
 * 详情：
 *
 * @author ChenCaihua
 * @since 2019年11月10日
 */
public class ClientBioDemo {

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 20; ++i) {

            try (Socket socket = new Socket(InetAddress.getLocalHost(), 9091)) {

                socket.getOutputStream().write(
                        ("[我是客户端请求消息" + System.currentTimeMillis() + "]").getBytes(StandardCharsets.UTF_8));
                //通过shutdownOutput告诉服务器已经发送完数据
                socket.shutdownOutput();

                StringBuilder resp = new StringBuilder();
                byte[] recvByteBuf = new byte[1024];
                int len;
                System.out.println("client开始读取返回数据，time = " + System.currentTimeMillis());
                while ((len = socket.getInputStream().read(recvByteBuf)) != -1) {
                    resp.append(new String(recvByteBuf, 0, len, StandardCharsets.UTF_8));
                }
                System.out.println("收到服务端返回消息: " + resp.toString() + ",time = " + System.currentTimeMillis());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Thread.sleep(40000);
    }
}
