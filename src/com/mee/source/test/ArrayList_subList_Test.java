package com.mee.source.test;

import com.mee.source.c1.ArrayList;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;

public class ArrayList_subList_Test {

//    public static void main(String[] args) {
//        ArrayList arr = new ArrayList();
//        arr.add("a"); // 0
//        arr.add("b");
//        arr.add("c");
//        arr.add("d"); // 3
//        arr.add("e");
//        arr.add("f"); // 4
//
//        List list = arr.subList(0, 3);
//        System.out.println(list);// [a, b, c]
//
//        for(int i = 0,c=arr.size();i<c;i++){
//            System.out.println("for loop-> "+arr.get(i));
//        }
//
//
//        for( Iterator iterator = arr.iterator();iterator.hasNext();){
//            Object next = iterator.next();
//            System.out.println("iter loop-> "+next);
//        }
//    }

//    public static void main(String[] args) {
//        ArrayList arr = new ArrayList();
//        arr.add("a"); // 0
//        arr.add("b");
//        arr.add("c");
//        arr.add("d"); // 3
//        System.out.println(arr);
//        List list = arr.subList(0, 3);
//        // sublist的set之前绝不能增加或减少ArrayList的元素，否则抛错
////        arr.add("H");
//        list.set(1,"B+");
//        System.out.println(arr);
//        System.out.println(list);
//
//        arr.add(null);
//        arr.stream().forEach(item->{
//            if("c".equals(item)){
//                return;
//            }
//            System.out.println("-->"+item);
//        });
//
//        System.out.println("---> Spliterator <---");
//        System.out.println(arr);
//        Spliterator spliterator = arr.spliterator();
////        Spliterator spliterator1 = spliterator.trySplit(); // 尝
//        spliterator.forEachRemaining(item-> System.out.println(item));
////        spliterator1.forEachRemaining(item-> System.out.println(item));
//
//    }


    public static void main(String[] args) {
        ArrayList arr = new ArrayList();
        arr.add("a");
        arr.add("b");
        arr.add(null);
        arr.add("c");
        arr.add("d");
        arr.add("h");
        Spliterator spliterator = arr.spliterator();
//        Spliterator spliterator1 = spliterator.trySplit();
//        spliterator.forEachRemaining(item-> System.out.println(item));
//        spliterator1.forEachRemaining(item-> System.out.println(item));

    }

}
