/**
 * @description: 邀请码
 * @create: 2020-06-03 15:52
 **/
package com.jsy.basic.util.utils;

import jdk.swing.interop.SwingInterOpUtils;
import org.aspectj.weaver.ast.Var;

import java.util.Random;

public class CodeUtils {

    private static final long OFFSET = 1577808000000L;

    private static final char[] r = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /** 定义一个字符用来补全邀请码长度（该字符前面是计算出来的邀请码，后面是用来补全用的） */
    private static final char b='0';

    /** 进制长度 */
    private static final int binLen = r.length;

    /** 邀请码长度 */
    private static final int s = 8;

    /**
     * 根据ID生成随机码
     * @param id ID
     * @return 随机码
     */
    public static String toSerialCode(long id) {
        char[] buf=new char[32];
        int charPos=32;

        while((id / binLen) > 0) {
            int ind=(int)(id % binLen);
            buf[--charPos]=r[ind];
            id /= binLen;
        }
        buf[--charPos]=r[(int)(id % binLen)];
        String str=new String(buf, charPos, (32 - charPos));
        // 不够长度的自动随机补全
        if(str.length() < s) {
            StringBuilder sb=new StringBuilder();
            sb.append(b);
            Random rnd=new Random();
            for(int i=1; i < s - str.length(); i++) {
                sb.append(r[rnd.nextInt(binLen)]);
            }
            str+=sb.toString();
        }
        return str;
    }

    /**
     * 根据随机码生成ID
     * @param code
     * @return ID
     */
    public static long codeToId(String code) {
        char chs[]=code.toCharArray();
        long res=0L;
        for(int i=0; i < chs.length; i++) {
            int ind=0;
            for(int j=0; j < binLen; j++) {
                if(chs[i] == r[j]) {
                    ind=j;
                    break;
                }
            }
            if(chs[i] == b) {
                break;
            }
            if(i > 0) {
                res=res * binLen + ind;
            } else {
                res=ind;
            }
        }
        return res;
    }

    public static String getDays(){
        return String.valueOf((System.currentTimeMillis()-OFFSET)/(24*60*60*1000));
    }

    /**
     * 按天产生  1048575后会产生重复
     * 秒数(当前时间总秒数7位16进制数)+序列(5位16进制字符)
     * 支持每秒 0xFFFFF 个并发
     * @return
     */
    public static String No(long no){
        if (no > 0xFFFFF) no = 0;
        String unique = String.format("%07X", (System.currentTimeMillis() - OFFSET) / 1000) + String.format("%05X", no);
        return unique.toUpperCase();
    }

    /**
     * 秒数(当前时间总秒数7位16进制数)+序列(5位16进制字符)
     * 支持每秒 999 个并发
     * @return
     */
    public static String getRepostNo(long no){
        if (no > 999) no = 0;
        String unique = "C" + String.format("%09d", (System.currentTimeMillis() - OFFSET) / 1000) + String.format("%03d", no);
        return unique.toUpperCase();
    }


    public static void main(String[] args) {
       System.out.println(getRepostNo(1234));
        System.out.println(toSerialCode(1234567778878891L));
        System.out.println(codeToId(toSerialCode(45454L)));
        Long Value=123456789101145L;
        long l = Long.parseLong(Value.toString().substring(0, 6));
        String s = toSerialCode(l);
        System.out.println(s);
    }
}
