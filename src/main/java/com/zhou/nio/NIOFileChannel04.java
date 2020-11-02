package com.zhou.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class NIOFileChannel04 {

    public static  void main(String[] args) throws Exception{


        FileInputStream fileInputStream = new FileInputStream("e:\\a.jpg");
        FileOutputStream fileOutputStream = new FileOutputStream("e:\\a2.jpg");

        FileChannel channel01 = fileInputStream.getChannel();
        FileChannel channel02 = fileOutputStream.getChannel();

        channel02.transferFrom(channel01,0,channel01.size());

        channel01.close();
        channel02.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
