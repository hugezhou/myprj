package com.zhou.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * read 从通道读取数据到缓冲区
 *
 * write 把缓冲区的数据写到通道
 */
public class ScatteringAndGatheroingTest {

    public static void main(String[] args) throws  Exception{

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        serverSocketChannel.socket().bind(inetSocketAddress);

        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        SocketChannel socketChannel = serverSocketChannel.accept();

        int messageLength = 8;

        while (true) {

            int byteRead = 0;
            while (byteRead < messageLength){
                long l = socketChannel.read(byteBuffers);
                byteRead += l;
                System.out.println("byteRead=" + byteRead);
                Arrays.asList(byteBuffers).stream().map(buffer->
                        "postion="+buffer.position() +
                        ",limit= " + buffer.limit()).forEach(System.out::println);
            }

            Arrays.asList(byteBuffers).forEach(buffer->buffer.flip());

            int byteWrite = 0;

            while (byteWrite < messageLength){
                long l = socketChannel.write(byteBuffers);
                byteWrite += l;
            }

            Arrays.asList(byteBuffers).forEach(buffer->buffer.clear());

            System.out.println("byteRead="+byteRead);
            System.out.println("byteWrite="+byteWrite);
            System.out.println("messageLength="+messageLength);
        }

    }
}
