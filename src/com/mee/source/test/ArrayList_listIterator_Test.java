package com.mee.source.test;

import com.mee.source.c1.ArrayList;

import java.util.Iterator;
import java.util.ListIterator;

public class ArrayList_listIterator_Test {

//    public static void main(String[] args) {
//        ArrayList a = new ArrayList();
//        a.add('a');
////        ListIterator listIterator = a.listIterator();
//        Iterator iterator = a.iterator();
//        iterator.remove();
//    }
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

//    public static void main(String[] args) {
//        ArrayList arr = new ArrayList();
//        arr.add("a");
//        arr.add("b");
//        arr.add("c");
//        System.out.println(arr);
//        Iterator iterator = arr.iterator();
//        iterator.next(); // a
//        iterator.forEachRemaining(item-> System.out.println(item)); // b c
//    }

//    public static void main(String[] args) {
//        ArrayList arr = new ArrayList();
//        arr.add("a");
//        arr.add("a2");
//        arr.add("b");
//        arr.add("c");
////        ListIterator listIterator = arr.listIterator();
////        listIterator.next();
////        listIterator.next();
////        boolean b = listIterator.hasPrevious();
////        System.out.println("hasPrevious()->"+b);
////        Object previous = listIterator.previous();
////        System.out.println("previous()->"+previous);
////        int i = listIterator.nextIndex();
////        System.out.println("i->"+i);
//
//
//        ListIterator listIterator1 = arr.listIterator();
//        while(listIterator1.hasNext()){
//            Object next = listIterator1.next();
//            System.out.println("value="+next);
////            if("b".equals(next)){
////                listIterator1.remove();
////            }
//            boolean b2 = listIterator1.hasPrevious();
//            System.out.println("hasPrevious()->"+b2);
//
//            int i2 = listIterator1.nextIndex();
//            System.out.println("i->"+i2+"");
//
//            int pre_idx = listIterator1.previousIndex();
//            System.out.println("pre_idx="+pre_idx);
//
//            System.out.println("\n------");
//        }
//        System.out.println(arr);
//    }


//    public static void main(String[] args) {
//        ArrayList arr = new ArrayList();
//        arr.add("a");
//        arr.add("b");
//        arr.add("c");
//        ListIterator listIterator = arr.listIterator();
//        arr.remove("a");
//        Object previous = listIterator.previous();
////        while (listIterator.hasNext()){
////
////        }
//    }

//    public static void main(String[] args) {
//        ArrayList arr = new ArrayList();
//        arr.add("a");
//        arr.add("b");
//        arr.add("c");
//        System.out.println("before:"+arr); // before:[a, b, c]
//        ListIterator listIterator = arr.listIterator();
////        while(listIterator.hasNext()){
////            Object item = listIterator.next();
////            System.out.println(item);
////            if("b".equals(item)){
////                listIterator.set("B");
////            }
////        }
//        listIterator.set("B"); // 这个是错误的...
//        System.out.println("after:"+arr); // after:[a, B, c]
//
//    }

    public static void main(String[] args) {
        ArrayList arr = new ArrayList();
        arr.add("a");
        arr.add("b");
        arr.add("c");
        System.out.println("before:"+arr); // before:[a, b, c]
        ListIterator listIterator = arr.listIterator();
        while (listIterator.hasNext()){
            Object item = listIterator.next();
            if("b".equals(item)){
                listIterator.add("b+");
            }
        }
        System.out.println("after:"+arr); // after:[a, B, c]
        arr.add(0,"A");
        System.out.println("after:"+arr); // after:[a, B, c]

    }

}
