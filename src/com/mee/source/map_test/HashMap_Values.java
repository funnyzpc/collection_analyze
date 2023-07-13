package com.mee.source.map_test;

import com.mee.source.c2.HashMap;

/**
 * values
 *
 * @author shaoow
 * @version 1.0
 * @className HashMap_Values
 * @date 2023/7/12 16:26
 */
public class HashMap_Values {
    public static void main(String[] args) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("aa",1);
        data.put("bb",Boolean.TRUE);
        data.values().forEach(System.out::println);
        data.values().forEach(item->{
           System.out.println(item);
        });
    }
}
