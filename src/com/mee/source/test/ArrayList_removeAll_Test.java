package com.mee.source.test;

import com.mee.source.arr.ArrayList;

public class ArrayList_removeAll_Test{

//    public static void main(String[] args) {
//        ArrayList l1 = new ArrayList();
//        l1.add("a");
//        l1.add("b");
//        l1.add("c");
//        l1.add("d");
//        l1.add("e");
//        l1.add("f");
//        ArrayList l2 = new ArrayList();
//        l2.add("a");
//        l2.add("b");
//        l2.add("c");
//        boolean b = l1.removeAll(l2);
//        System.out.println("返回结果："+b);
//    }

    public static void main(String[] args) {
        ArrayList l1 = new ArrayList();
        l1.add("a");
        l1.add("b");
        l1.add("c");
        l1.add("d");
        l1.add("e");
        l1.add("f");
        ArrayList l2 = new ArrayList();
        l2.add("a");
        l2.add("b");
        l2.add("c");
        boolean b = l1.retainAll(l2);
        System.out.println("返回结果："+b);
        System.out.println("l1.size()="+l1.size());
        System.out.println("l2.size()="+l2.size());
    }

//    public static void main(String[] args) {
//        int i = 0;
//        for(;i<5;i++){
////        for(;i<5;++i){
//            System.out.println("idx="+i);
//        }
//        System.out.println("finally idx="+i);
//    }



}
