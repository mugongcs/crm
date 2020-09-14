package com.javastudy.settings.test;

import com.javastudy.crm.utils.DateTimeUtil;

import java.util.Date;

public class test1 {
    public static void main(String[] args) {
        String expireTime = "2019-10-10 10:10:10";

        Date date = new Date();
        String currentTime = DateTimeUtil.getSysTime();

        int count = expireTime.compareTo(currentTime);
        if ("0".equals("2"))
        System.out.println(count);
    }
}
