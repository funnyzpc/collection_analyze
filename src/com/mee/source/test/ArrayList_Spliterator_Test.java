package com.mee.source.test;

import com.mee.source.c1.ArrayList;

import java.util.Spliterator;

public class ArrayList_Spliterator_Test {


    public static void main(String[] args) {
        ArrayList arr = new ArrayList();
        arr.add("a");
        arr.add("b");
        arr.add("c");
        arr.add(null);
        arr.add("e");
        arr.add("h");
        arr.add("i");
        ArrayList lst =new ArrayList<>();
        Spliterator spliterator = arr.spliterator();
        lst.add(spliterator);
//        spliterator.()
//        while(spliterator.hasCharacteristics(3)){
//            Spliterator spliterator1 = spliterator.trySplit();
//            lst.add(spliterator1);
//        }
        System.out.println(lst);

    }

}
