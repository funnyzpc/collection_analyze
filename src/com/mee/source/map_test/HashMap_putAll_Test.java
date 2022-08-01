package com.mee.source.map_test;

import com.mee.source.c2.HashMap;

public class HashMap_putAll_Test {

    public static void main(String[] args) {
        HashMap<String,String> m1 = new HashMap<String,String>();
        m1.put("A","1");
        m1.put("B","2");
        m1.put("C","3");

        HashMap<String,String> m2 = new HashMap<String,String>();
        m2.put("A","K");
        m2.put("B","K");
        m2.put("D","4");

        m1.putAll(m2);

        System.out.println(m1); // {A=K, B=K, C=3, D=4}
        System.out.println(m2); // {A=K, B=K, D=4}

        String d = m2.remove("D");
        System.out.println(d);
    }


}
