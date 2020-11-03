package com.zhou.nio;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class NioClient {


    public static void main(String[] args) throws Exception{

        SocketChannel socketChannel = SocketChannel.open();

//        socketChannel.socket().bind("127.0.0.1",6666);
//
//        socketChannel.bind(InetSocketAddress("127.0.0.1",6666));

    }
}
