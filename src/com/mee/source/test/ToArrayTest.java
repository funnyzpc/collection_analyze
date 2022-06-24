package com.mee.source.test;

import com.mee.source.c1.ArrayList;

public class ToArrayTest {


//    public static void main(String[] args) {
//        ArrayList<String> srcList = new ArrayList<>(8);
//        srcList.add("aa");
//        srcList.add("gg");
//        srcList.add("oo");
//        srcList.add("hh");
//        String[] desArr = new String[8];
//        desArr[5] = null;
//        desArr[6]="EOF";
//        System.out.println(System.identityHashCode(desArr));
//        String[] strings = srcList.toArray(desArr);
//        System.out.println(System.identityHashCode(strings));
//        System.out.println(strings);
//    }

    public static void main(String[] args) {
        ArrayList<String> srcList = new ArrayList<>(8);
        srcList.add("aa");
        String s = srcList.get(-1);
        System.out.println(s);
    }

}
