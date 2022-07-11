package com.mee.source.test;

import com.mee.source.c1.ArrayList;

import java.util.Iterator;
import java.util.ListIterator;


public class ArrayList_remve_Test {

//    public static void main(String[] args) {
//        ArrayList arr  = new ArrayList();
//        arr.add("a");
//        arr.add("b");
//        arr.add("c");
//        arr.add("d");
//        arr.add("e");
//        System.out.println(arr);
//        arr.remove()
//
////        for(Object item:arr){
////            if("d".equals(item)){
////                arr.remove(item);
////            }
////        }
//

//
////        ListIterator lst_itr = arr.listIterator();
////        while(lst_itr.hasNext()){
////            int idx = lst_itr.nextIndex();
////            Object item = lst_itr.next();
////            System.out.println(idx+"->"+item);
////            if("d".equals(item)){
////                lst_itr.remove();
////            }
////        }
//
////        for(ListIterator lst_itr = arr.listIterator();lst_itr.hasNext();){
////            int idx = lst_itr.nextIndex();
////            Object item = lst_itr.next();
////            System.out.println(idx+"->"+item);
////            if("d".equals(item)){
////                lst_itr.remove();
////            }
////        }
//
////        int i = 0;
////        for(ListIterator lst_itr = arr.listIterator();lst_itr.hasNext();i++){
////            int idx = lst_itr.nextIndex();
////            Object item = lst_itr.next();
////            System.out.println(i+" -> "+idx+":"+item);
////            if("d".equals(item)){
////                lst_itr.remove();
////            }
////        }
//
////        arr.removeIf(item->null!=item&&"d".equals(item));
//
//        for(int i=0,c=arr.size();i<arr.size();i++){
//            if("d".equals(arr.get(i))){
//                if(arr.remove("d") && i==(c-1)){
//                    break;
//                }
//            }
//        }
//
//        System.out.println(arr);
//
//    }


//    public static void main(String[] args) {
//        ArrayList arr  = new ArrayList();
//        arr.add("a");
//        arr.add("b");
//        arr.add("c");
//        arr.add("d");
//        arr.add("e");
//        System.out.println(arr);
//        arr.remove("c");// remove c
//        arr.remove(3);// remove d
//        System.out.println(arr);
//    }

//    public static void main(String[] args) {
//        ArrayList arr  = new ArrayList();
//        arr.add("a");
//        arr.add("b");
//        arr.add("c");
//        arr.add("d");
//        arr.add("e");
//        arr.add("d");
//        System.out.println(arr);
//        for(int i=0;i<arr.size();i++){
//            Object item = arr.get(i);
//            if("d".equals(item)){
//                System.out.println(arr.size());
//                arr.remove("d");
//                System.out.println(arr.size());
//            }
//        }
//        System.out.println("end...:"+arr);
//
//    }

//    public static void main(String[] args) {
//
//        ArrayList arr  = new ArrayList();
//        arr.add("a");
//        arr.add("b");
//        arr.add("c");
//        arr.add("d");
//        arr.add("e");
//        arr.add("d");
//        System.out.println(arr);
//        for(int i=0;i<6;i++){
//            if( i==(arr.size()-1) ){
//                break;
//            }
//            if(arr.get(i).equals("d")){
//                arr.remove(i);
//            }
//        }
//        System.out.println(arr);
//    }


//    public static void main(String[] args) {
//        ArrayList arr  = new ArrayList();
//        arr.add("a");
//        arr.add("b");
//        arr.add("c");
//        arr.add("d");
//        arr.add("e");
//        arr.add("d");
//        System.out.println(arr);
//        Iterator itr = arr.listIterator();
//        while(itr.hasNext()){
//            Object item = itr.next();
//            if("d".equals(item)){
//                itr.remove();
//            }
//        }
//        System.out.println(arr);
//    }

    public static void main(String[] args) {
        ArrayList arr  = new ArrayList();
        arr.add("a");
        arr.add("b");
        arr.add("c");
        arr.add("d");
        arr.add("e");
        arr.add("d");
        System.out.println(arr);
        arr.removeIf(item->"d".equals(item));
        System.out.println(arr);
    }

}
