package com.mee.source.map_test;


import java.util.Objects;

public class HashMap_hash_Test {

    public static void main(String[] args) {
//        String a = "hello";
        int a = 'A';
        int b = 1;
        System.out.println(Objects.hash(a));
        System.out.println(Objects.hash(b));
        System.out.println(Objects.hash(a) ^ Objects.hash(b));
    }
}
