package com.mee.source.map_test;

import com.mee.source.c2.HashMap;

/**
 * merge
 *
 * @author shaoow
 * @version 1.0
 * @className HashMap_merge
 * @date 2023/7/13 15:47
 */
public class HashMap_merge {
    public static void main(String[] args) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("aa",1);
        data.put("bb",Boolean.TRUE);
//        data.merge("aa",99,(k,v)->123);
        data.merge("aa2",99,(k,v)->123);
        System.out.println(data);
        data.forEach((k,v)->{
            System.out.println(k+":"+v);
        });
        //data.replaceAll((k,v)->99);
        data.replaceAll((k,v)-> {
            return 99;
        });
        //System.out.println(data);

    }
}
