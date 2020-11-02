package com.zhou.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer 好处内存中修改，堆外内润
 */
public class MapperByteBuffer {

    public static void main(String[] args) throws Exception{


        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt","rw");

        FileChannel  fileChannel = randomAccessFile.getChannel();

        MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.
                READ_WRITE,0,5);


        mappedByteBuffer.put(0,(byte) 'Y');

        randomAccessFile.close();

    }
}
