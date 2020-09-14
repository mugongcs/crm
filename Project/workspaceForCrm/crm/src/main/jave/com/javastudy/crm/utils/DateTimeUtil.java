package com.javastudy.crm.utils;

import java.text.SimpleDateFormat;
import java.util.Date;


public class DateTimeUtil {
    /**
     * @return 2020-9-3 10:38:29格式的系统当前时间
     */
    public static String getSysTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = new Date();

        String dateStr = sdf.format(date);
        return dateStr;
    }
}
