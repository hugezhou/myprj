package com.zhou.nio.groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {

    private ServerSocketChannel listenChannel;
    private Selector selector;
    private static final Integer PORT = 6667;

    public GroupChatServer(){



        try {
            selector = Selector.open();

            listenChannel = ServerSocketChannel.open();

            listenChannel.socket().bind(new InetSocketAddress(PORT));

            listenChannel.configureBlocking(false);

            listenChannel.register(selector, SelectionKey.OP_ACCEPT);



        }catch (IOException e){

        }



    }

    public void listen(){
        try{

            while (true){
                int count = selector.select(2000);
                if (count > 0){
                    Iterator<SelectionKey> keyIterator
                            = selector.selectedKeys().iterator();

                    while (keyIterator.hasNext()){

                        SelectionKey key = keyIterator.next();

                        if (key.isAcceptable()){

                            SocketChannel sc = listenChannel.accept();

                            sc.register(selector,SelectionKey.OP_READ);

                            sc.configureBlocking(false);

                            System.out.println(sc.getRemoteAddress()+" 上线..");
                        }
                        if (key.isReadable()){

                            readData(key);
                        }
                        keyIterator.remove();
                    }


                }else {
                    System.out.println("waiting....");
                }
            }
        }catch (Exception e){

        }
    }


    public void readData(SelectionKey key){

        SocketChannel socketChannel = null;


        try {
            socketChannel = (SocketChannel)key.channel();


            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

            Integer count = socketChannel.read(byteBuffer);

            if (count > 0){

                String msg = new String(byteBuffer.array());

                sendInfoToOtherClients(msg,socketChannel);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void sendInfoToOtherClients (String msg,
                                        SocketChannel self) throws IOException{

        System.out.println("服务器发消息中。。。");

        for (SelectionKey key:selector.keys()){

            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != self){

                SocketChannel dest = (SocketChannel)targetChannel;

                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());

                dest.write(byteBuffer);

            }
        }

    }



    public static void main(String[] args){
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }
}
