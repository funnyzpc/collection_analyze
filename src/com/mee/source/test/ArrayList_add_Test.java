package com.mee.source.test;

import com.mee.source.c1.ArrayList;

public class ArrayList_add_Test {

    public static void main(String[] args) {
        ArrayList<String> srcList = new ArrayList<String>(9);
        srcList.add("aa");
        srcList.add("gg");
        srcList.add("oo");
        srcList.add("hh");
        System.out.println(System.identityHashCode(srcList));
        srcList.add(2,"XX");
        System.out.println(System.identityHashCode(srcList));
        System.out.println(srcList);
    }
}
