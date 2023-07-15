package com.mee.source.map_test;

import com.mee.source.c2.HashMap;

import java.util.Iterator;

/**
 * 迭代器
 *
 * @author shaoow
 * @version 1.0
 * @className HashMap_iterator
 * @date 2023/7/14 16:43
 */
public class HashMap_iterator {

    public static void main(String[] args) {
        HashMap data = new HashMap(8);
        data.put("aa",1);
        data.put("bb",Boolean.TRUE);
        Iterator iterator = data.entrySet().iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
            iterator.remove();
        }
        System.out.println(data);

    }
}
