package com.mee.source.map_test;

import com.mee.source.c2.HashMap;

/**
 * clone
 *
 * @author shaoow
 * @version 1.0
 * @className HashMap_clone
 * @date 2023/7/13 17:02
 */
public class HashMap_clone {

    public static void main(String[] args) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("aa",1);
        data.put("bb",Boolean.TRUE);
        System.out.println(data);
        Object clone = data.clone();
        System.out.println(clone);

    }
}
