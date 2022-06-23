package com.mee.source.test;

import com.mee.source.arr.ArrayList;

import java.util.Iterator;
import java.util.ListIterator;

public class ArrayList_listIterator_Test {

    public static void main(String[] args) {
        ArrayList a = new ArrayList();
        a.add('a');
//        ListIterator listIterator = a.listIterator();
        Iterator iterator = a.iterator();
        iterator.remove();
    }
//    public int i=88;
//    public static void main(String[] args) {
//        int this_i = new ArrayList_listIterator_Test().i;
//        System.out.println(this_i);
//         new ArrayList_listIterator_Test().test01();
//    }
//
//
//    private void test01(){
//        int i2 = ArrayList_listIterator_Test.this.i;
//        System.out.println("i2="+i2);
//    }

//    public static void main(String[] args) {
//        String[] ar = {"a","b","c",null};
//        String[] ar2 = ar;
//        ar2[3]  ="hello";
//        System.out.println(ar[3]);
//        System.out.println(ar2[3]);
//    }
}
