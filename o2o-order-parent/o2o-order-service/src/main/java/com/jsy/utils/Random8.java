package com.jsy.utils;

import java.util.Random;

/**
 * @className（类名称）: Random8
 * @description（类描述）: this is the Random8 class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/8
 * @version（版本）: v1.0
 */
public class Random8 {
    public static String getNumber8(){
        StringBuilder str=new StringBuilder();//定义变长字符串
        Random random=new Random();
        //随机生成数字，并添加到字符串
        for(int i=0;i<11;i++){
            str.append(random.nextInt(10));
        }
        //将字符串转换为数字并输出
        //int num=Integer.parseInt(str.toString());
        System.out.println(str);
        return str.toString();
    }
    public static void main(String[] args) {
        String number8 = getNumber8();
    }
}
