package com.mee.source.test;

public class Test1 {


    public static void main(String[] args) {
        int a =-3;
        int b =4;
        System.out.println("before::a="+a+" b="+b);
        a = a^b;
        b = b^a;
        a = a^b;
        System.out.println("after::a="+a+" b="+b);
    }
}
