package com.mee.source.map_test;


import com.mee.source.c2.HashMap;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class HashMap_remove_Test {
    public static void main(String[] args) {
        HashMap map = new HashMap();
        map.put("a",1);
        map.put("b",2);
        map.put("c",3);
        map.put("d",4);
        map.put("e",5);

//        Set set = map.keySet();
//        for(Object s:set){
//            if("c".equals(s)){
//                map.remove(s);
//            }
//            // System.out.println(s);
//        }

        map.forEach((k,v)->{
                    System.out.println(k+"->"+v);
                    if("b".equals(k)){
                        map.remove(k);
                    }
        } );
        System.out.println(map);
    }
}
