package com.mee.source.test;

import com.mee.source.c1.ArrayList;

import java.util.List;

public class ArrayList_subList_Test {

    public static void main(String[] args) {
        ArrayList arr = new ArrayList();
        arr.add("a"); // 0
        arr.add("b");
        arr.add("c");
        arr.add("d"); // 3
        arr.add("e");
        arr.add("f"); // 4

        List list = arr.subList(0, 3);
        System.out.println(list);

    }


}
