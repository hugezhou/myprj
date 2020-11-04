package com.zhou.nio.groupChat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ThreadPoolExecutor;

public class GroupChatClient {

    private final String HOST = "127.0.0.1";

    private final Integer PORT = 6667;

    private SocketChannel socketChannel;

    private Selector selector;

    private String userName;




    public GroupChatClient() throws IOException{

        selector = Selector.open();

        socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));

        socketChannel.configureBlocking(false);

        socketChannel.register(selector, SelectionKey.OP_READ);

        userName = socketChannel.getLocalAddress().toString().substring(1);

        System.out.println(userName);

    }

    public void sendData(String info){

        info = userName + "说: " + info;

        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void readInfo(){

        try {
            Integer select = selector.select(2000);
            if (select > 0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()){
                    SelectionKey key = iterator.next();
                    if (key.isReadable()){

                        SocketChannel channel = (SocketChannel)key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        channel.read(byteBuffer);
                        String msg = new String(byteBuffer.array());
                        System.out.println(msg.trim());

                    }
                    iterator.remove();
                }
            }else {
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        GroupChatClient chatClient = new GroupChatClient();

        new Thread(){
            @Override
            public void run() {
                while (true){
                    chatClient.readInfo();

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){

            String info = scanner.nextLine();
            chatClient.sendData(info);

        }


    }
}
