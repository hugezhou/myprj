package com.zhou.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 每个客户端服务请求都需要一个独立的线程
 *
 * 当并发量较大的时候,系统资源占用率较大
 *
 * 连接建立后，系统没有数据可读，会阻塞在read处，造成资源的浪费
 *
 * block（阻塞）I/O  同步阻塞
 *
 */
public class BIoServer {

    public static void main(String[] args) throws Exception{

        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动了");

        while (true){


            System.out.println("accept.....");
            final Socket socket = serverSocket.accept();

            System.out.println("连接到一个客户端");

            newCachedThreadPool.execute(new Runnable() {
                public void run() {
                    handler(socket);
                }
            });
        }
    }


    public static void handler(Socket socket){

        System.out.println("Thread id:"+Thread.currentThread().getId());
        System.out.println("Thread name:"+Thread.currentThread().getName());

        byte[] bytes =  new byte[1024];
        try {
            InputStream inputStream = socket.getInputStream();

            while (true){

                System.out.println("read....");
                int read = inputStream.read(bytes);
                if (read != -1){
                    System.out.println("Thread id:"+Thread.currentThread().getId());
                    System.out.println("Thread name:"+Thread.currentThread().getName());
                    System.out.println(new String(bytes,0,read));
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.print("关闭和client的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
