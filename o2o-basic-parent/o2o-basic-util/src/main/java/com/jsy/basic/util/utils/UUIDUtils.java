package com.jsy.basic.util.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class UUIDUtils {

    public static String getUUID() {
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        return uuid;
    }

    //金额流水单号
    public static String getAccountNumber() {
        int code = UUID.randomUUID().toString().hashCode();
        code = code < 0 ? -code : code;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String s = format.format(new Date());
        return s + code;
    }
    //订单编号：4位随机数+uninx时间戳(10位)+门店行政区划代码(后2位)+门店编号后2位
    //return   4位随机数+uninx时间戳(10位)
    public static String getOrderNum(){
        String l = String.valueOf(ThreadLocalRandom.current().nextInt(1000,10000-1));
        String s = Long.toString(System.currentTimeMillis() / 1000L);
        return l+s;
    }

    //服务验证码：4位随机数+uninx时间戳后1,2位+订单编号前2位+uninx时间戳后3,4位
    //return   4位随机数+uninx时间戳后1,2位+订单编号前2位+uninx时间戳后3,4位
    public static String getServiceCode(String orderNum){
        String l = String.valueOf(ThreadLocalRandom.current().nextInt(1000,10000-1));
        String s = Long.toString(System.currentTimeMillis() / 1000L);

        return l+s.substring(0,2)+orderNum.substring(0,2)+s.substring(2,4);
    }
    public static String getTimeAndRandomSix(){
        String s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()).replaceAll("-", "").replaceAll(":", "").replaceAll(" ", "");
        int i = ThreadLocalRandom.current().nextInt(100 * 1000, 1000 * 1000 - 1);
        return s+i;
    }
}
