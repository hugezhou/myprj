package com.zhou.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 *
 */
public class NioClient {


    public static void main(String[] args) throws Exception{

        SocketChannel socketChannel = SocketChannel.open();

        socketChannel.configureBlocking(false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1",6666);

        if (!socketChannel.connect(inetSocketAddress)){

            while (!socketChannel.finishConnect()){
                System.out.println("......");
            }
        }

        //如果连接成功

        String str = "hi,hi";

        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());

        socketChannel.write(byteBuffer);

        System.in.read();


    }
}
