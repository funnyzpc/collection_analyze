package com.mee.source.map_test;

import com.mee.source.c2.HashMap;

/**
 * hash
 *
 * @author shaoow
 * @version 1.0
 * @className HashMap_hash
 * @date 2023/7/12 15:20
 */
public class HashMap_hash {

    public static void main(String[] args) {
        test01();
        System.out.println("hello".hashCode());
        System.out.println(HashMap.hash("hello"));

        System.out.println("youth".hashCode());
        System.out.println(HashMap.hash("youth"));
        System.out.println(HashMap.hash("youth")>>10);

//        HashMap<String, Object> data = new HashMap<>();
//        data.put("aa",1);
//        data.put("bb",Boolean.TRUE);
//       data.keySet().forEach(System.out::println);
//       data.keySet().forEach(item->{
//           System.out.println(item);
//       });

    }

    public static void test01(){
        for( int i =0;i<16;i++ ){
            System.out.println(HashMap.hash(i+"")+" string hash:"+new String(1+"").hashCode());
        }
    }
}
