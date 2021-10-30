package com.jsy.basic.util.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

 /**
  * @Author lxr
  * @Description 图片id字符串 转换为 图片url字符串工具类
  * @Date 2021/1/26 16:29
  **/
public class ImagesToList {

    /**
     * 拆分 ,隔开的图片url地址
     * @param images
     * @return
     */
    public static ArrayList<String> ImagesList(String images) {
        Map<String,Object> map = new HashMap();
        boolean more = false;
        ArrayList<String> urls = new ArrayList<>();
        String[] split = images.split(",");
        if (split.length > 1) {
            for (String s : split) {
                urls.add(s);
            }
            more = true;
        } else {
            urls.add(images);
        }

        return urls;
    }

    /**
     * 返回图片url地址
     * @param images
     * @param url
     * @return
     */
    public static String ToStringUrl(String images,Map<String,String> url) {

        String[] split = images.split(",");

        if (split.length > 1) {
            StringBuffer buffer = new StringBuffer();
            for (int x = 0; x < split.length; x++) {
                if (x == split.length - 1) {
                    buffer.append(url.get(split[x]));
                    return buffer.toString();
                }
                buffer.append(url.get(split[x]) + ",");
            }
        }
        return url.get(images);
    }

}
