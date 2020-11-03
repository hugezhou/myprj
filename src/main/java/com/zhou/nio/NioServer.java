package com.zhou.nio;

import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * nio (non-blocking io) 同步非阻塞
 *
 * nio 三大核心部分 channel(通道)，buffer(缓冲区)，selector(选择器)
 *
 * nio是面向缓冲区，或者面向块编程的
 *
 *
 * Java NIO中的 ServerSocketChannel 是一个可以监听新进来的TCP连接的通道,
 * 就像标准IO中的ServerSocket一样。
 * ServerSocketChannel类在 java.nio.channels包中。
 */
public class NioServer {

    public static void main(String[] args) throws Exception{

        //创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector
        Selector selector = Selector.open();

        //绑定一个端口6666，在服务器监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //将serverSocketChannel注册到 selector，关心的事件为OP_ACCEPT
        serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);



        while (true){

            if (selector.select(1000)==0){
                System.out.println("服务器等待了1秒，无连接");
                continue;
            }

            Set<SelectionKey> selectionKeys  = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){

                SelectionKey key = keyIterator.next();

                if (key.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    socketChannel.configureBlocking(false);

                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));


                }
                if (key.isReadable()){

                    SocketChannel socketChannel = (SocketChannel)key.channel();

                    ByteBuffer byteBuffer = (ByteBuffer)key.attachment();

                    socketChannel.read(byteBuffer);

                    System.out.println("from client"+new String(byteBuffer.array()));

                }

                //手动从集合总移动当前的SelectionKey,防止重复操作
                keyIterator.remove();


            }


        }




    }

}
