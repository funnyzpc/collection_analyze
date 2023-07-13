package com.mee.source.map_test;

import com.mee.source.c2.HashMap;

/**
 * computeIfPresent
 *
 * @author shaoow
 * @version 1.0
 * @className HashMap_computeIfPresent
 * @date 2023/7/13 13:50
 */
public class HashMap_computeIfPresent {
    public static void main(String[] args) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("aa",1);
        data.put("bb",Boolean.TRUE);
        //data.computeIfPresent("aa",(k,v)->12);
        data.computeIfPresent("aa",(k,v)->null);
        System.out.println(data);
    }
}
