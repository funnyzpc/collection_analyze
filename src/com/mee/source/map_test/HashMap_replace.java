package com.mee.source.map_test;

import com.mee.source.c2.HashMap;

/**
 * 替换值
 *
 * @author shaoow
 * @version 1.0
 * @className HashMap_replace
 * @date 2023/7/12 17:01
 */
public class HashMap_replace {
    public static void main(String[] args) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("aa",1);
        data.put("bb",Boolean.TRUE);
        data.replace("aa",1,Boolean.FALSE);
        System.out.println(data);
    }
}
