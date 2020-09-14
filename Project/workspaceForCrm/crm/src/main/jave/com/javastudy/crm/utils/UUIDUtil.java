package com.javastudy.crm.utils;

import java.util.UUID;

public class UUIDUtil {
    /**
     * @return 32位的String类型字符串
     * @deprecated 生成一个32的UUID作为主键
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
