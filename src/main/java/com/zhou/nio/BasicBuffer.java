package com.zhou.nio;

import java.nio.IntBuffer;

/**
 *     Buffer 下的属性
 *     private int mark = -1; 标记
 *     private int position = 0; 位置标记
 *     private int limit; 缓冲区的当前终点
 *     private int capacity; 容量
 */
public class BasicBuffer {

    public static void main(String[] args){


        IntBuffer intBuffer = IntBuffer.allocate(5);

        for (int i=0; i < 5;i++){
            intBuffer.put(i * 2);
        }

        //读写切换 position = 0;
        intBuffer.flip();

        for (int i=0; i < 5;i++){
            System.out.println(intBuffer.get(i));
        }
    }
}
