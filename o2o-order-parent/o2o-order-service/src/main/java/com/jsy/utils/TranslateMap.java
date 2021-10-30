package com.jsy.utils;

import com.jsy.basic.util.annotation.MapForExecl;
import com.jsy.basic.util.exception.JSYException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yu
 *
 */
public class TranslateMap {

    private static Map<String,Object> map = new HashMap<>();

    public TranslateMap() {
    }

    public static Map translateOrder(Class o){
        map.clear();
        //获取类的字段
        try{
            Field[] fields = o.getDeclaredConstructor().newInstance().getClass().getDeclaredFields();
            for(int i=0;i<fields.length;i++){
                map.put(fields[i].getName(),fields[i].getAnnotation(MapForExecl.class).description());
            }
            return map;
        }catch (Exception e){
            throw  new JSYException(-1,"对象转换错误");
        }
    }

    public static boolean checkOrder(Object o1, Object o2){
        Field[] declaredFields = o1.getClass().getDeclaredFields();
        Field[] declaredFields2 = o2.getClass().getDeclaredFields();
        for (int i=0 ;i<declaredFields.length;i++){
            declaredFields[i].setAccessible(true);
            declaredFields2[i].setAccessible(true);
            //String name = declaredFields[i].getName();
            try {
                //System.out.println(declaredFields[i].get(o1));
                //System.out.println(declaredFields2[i].get(o2));
                if (!Objects.isNull(declaredFields[i].get(o1))&&!Objects.isNull(declaredFields[i].get(o2))){
                    if (!declaredFields[i].get(o1).equals(declaredFields2[i].get(o2))){
                        return false;
                    }
                }
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block  
                e.printStackTrace();
            }catch(IllegalAccessException e){
                // TODO Auto-generated catch block  
                e.printStackTrace();
            }
        }
        return true;
    }

}
