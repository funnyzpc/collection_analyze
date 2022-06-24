package com.mee.source.test;


import java.util.Arrays;

public class ArrayListTest {


//    public static void main(String[] args) {
//        ArrayList list= new ArrayList(); // 构造一个 空的 Object[]={};
//        list.add("111");
//        list.add("222");
//        System.out.println(list);
//    }

//    public static void main(String[] args) {
//        ArrayList list= new ArrayList(); // 构造一个 空的 Object[]={};
//        for(int i = 0;i<12;i++){
//           if(i==10){
//               list.add("idx_"+i);
//           }else{
//               list.add("idx_"+i);
//           }
//        }
//        System.out.println(list);
//    }

//    // 测试 Arrays.toArray(src,len)
//    public static void main(String[] args) {
//        List<String> lst1 = new ArrayList<String>();
//        lst1.add("aaa");
//        lst1.add("bbb");
//        ArrayList<String> lst2 = new ArrayList<>(lst1);
//        lst1.add("CCC");
//        System.out.println(lst1);
//        System.out.println(lst2);
//    }

//    public static void main(String[] args) {
//        ArrayList list= new ArrayList(); // 构造一个 空的 Object[]={};
//        list.remove(-1);
//    }

//    public static void main(String[] args) {
//        ArrayList<String> lst1 = new ArrayList<String>();
//        lst1.add("aaa");
//        lst1.add("bbb");
//        ArrayList lst2 = (ArrayList) lst1.clone();
//        lst1.add("!!!");
//        lst2.add("ccc");
//        System.out.println(lst1);
//        System.out.println(lst2);
//    }

//    public static void main(String[] args) {
//        ArrayList<Object> lst1 = new ArrayList<Object>();
//        lst1.add("aaa");
//        lst1.add("bbb");
//        ArrayList lst2 = lst1;
//        lst1.add("!!!");
//        lst2.add("ccc");
//        System.out.println(lst1);
//        System.out.println(lst2);
//    }

//    public static void main(String[] args) {
//        StringJoiner sj  =new StringJoiner(":");
//        sj.add("a");
//        ArrayList<Object> lst1 = new ArrayList<Object>();
//        lst1.add("aaa");
//        lst1.add(sj);
//        ArrayList lst2 = (ArrayList) lst1.clone();
////        lst2.add(sj);
//        sj.add("b");
//        lst1.add("!!!");
//        sj.add("c");
//        lst2.add("ccc");
//        System.out.println(lst1);
//        System.out.println(lst2);
//    }

//    public static void main(String[] args) {
//
//        StringJoiner sj  =new StringJoiner(":");
//        sj.add("x");
//
//        Object[] obj1 = {59,sj};
//        Object[] obj2 = Arrays.copyOf(obj1, obj1.length);
////        Object[] obj2 = obj1;
//        sj.add("y");
//
//        System.out.println(System.identityHashCode(obj1));
//        System.out.println(System.identityHashCode(obj1[0]));
//        System.out.println(System.identityHashCode(obj1[1]));
//        System.out.println(System.identityHashCode(new Integer(999)));
//
//        System.out.println("----><------");
//
//        System.out.println(System.identityHashCode(obj2));
//        System.out.println(System.identityHashCode(obj2[0]));
//        System.out.println(System.identityHashCode(obj2[1]));
//        System.out.println(System.identityHashCode(new Integer(999)));
//
//
//        Integer[] h = {11,222};
//        int[] j={11,222};
//        System.out.println(h);
//        System.out.println(j);
//    }

//    public static void main(String[] args) {
//        Integer i = new Integer(127);
//        Integer j = new Integer(127);
//        System.out.println(System.identityHashCode(i));
//        System.out.println(System.identityHashCode(j));
//    }


    public static void main(String[] args) {

        int[] obj1 = {59,222};
        int[] obj2 = Arrays.copyOf(obj1, obj1.length);
//        Object[] obj2 = obj1;

        System.out.println(System.identityHashCode(obj1));
        System.out.println(System.identityHashCode(obj1[0]));
        System.out.println(System.identityHashCode(obj1[1]));

        System.out.println("----><------");

        System.out.println(System.identityHashCode(obj2));
        System.out.println(System.identityHashCode(obj2[0]));
        System.out.println(System.identityHashCode(obj2[1]));



    }


}
