package com.mee.source.map_test;

import com.mee.source.c2.HashMap;

import java.util.HashSet;

public class HashMap_tableSizeFor_Test {

//    public static void main(String[] args) {
//
//        HashMap h = new HashMap(9);
//        // initialCapacity=0 => threshold=1
//        // initialCapacity=1 => threshold=1
//        // initialCapacity=2 => threshold=2
//        // initialCapacity=3 => threshold=4
//        // initialCapacity=4 => threshold=4
//        // initialCapacity=5 => threshold=8
//        // initialCapacity=9 => threshold=16
////        h.threshold;
//        System.out.println(h);
//    }


    public static void main(String[] args) {
//        int i = 1;
//        int i = 15;
//        int i = (1<<30)+1;
//        int i = 1<<30;
//        int i = 1073741824;
//        int i = 0x40000000;
//        int i = 100;
//        int i = 1<<16; // 65536 -> 65536
//        int i = 1<<16 + 1; // 65537 -> 131072
//        int i = (1<<17) + 1; // 131073 -> 262144
//        int i = (1<<17) + 11; // 131083 -> 262144
//        int i = (1<<18) + 1; // 131083 -> 262144
        int i = (1<<22) + 1; //  4194305 -> 8388608
        int result = tableSizeFor(i);
        System.out.println(result);
        HashMap h = new HashMap(111);
        h.put("a",1);
        h.put("b",2);
        System.out.println(h.size());
    }

    static final int MAXIMUM_CAPACITY = 1 << 30;// 最大容量

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }
}
