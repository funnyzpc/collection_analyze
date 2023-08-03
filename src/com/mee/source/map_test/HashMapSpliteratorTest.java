package com.mee.source.map_test;

import com.mee.source.c2.HashMap;

import java.util.Spliterator;

/**
 * HashMapSpliterator
 *
 * @author shaoow
 * @version 1.0
 * @className HashMapSpliteratorTest
 * @date 2023/7/24 10:56
 */
public class HashMapSpliteratorTest {

    public static void main(String[] args) {
        HashMap<String, Object> data = new HashMap<>(4);
        data.put("aa",1);
        data.put("bb",Boolean.TRUE);

//        Spliterator<String> stringSpliterator = data.keySet().spliterator().trySplit();
//        System.out.println(stringSpliterator);
//        data.keySet().spliterator().forEachRemaining(item-> System.out.println(item));
//        boolean result = data.keySet().spliterator().tryAdvance(item -> System.out.println(item));
//        System.out.println(result);
//        int characteristics = data.keySet().spliterator().characteristics();
//        System.out.println(characteristics);
        Spliterator<Object> objectSpliterator = data.values().spliterator().trySplit();
        objectSpliterator.forEachRemaining(item-> System.out.println(item));
        int characteristics = objectSpliterator.characteristics();
        System.out.println(characteristics);

    }
}
