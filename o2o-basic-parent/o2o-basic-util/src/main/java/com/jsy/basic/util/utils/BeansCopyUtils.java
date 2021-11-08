package com.jsy.basic.util.utils;

import cn.hutool.core.bean.BeanUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final   class BeansCopyUtils {
    private BeansCopyUtils() {
    }

    public static <T1, T2> List<T2> listCopy(List<T1> sourceList, Class<T2> clazz) {
        return (List<T2>) sourceList.stream().map((source)->{
            Object target;
            try {
                target = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException();
            }
            BeanUtil.copyProperties(source, target);
            return target;
        }).collect(Collectors.toList());
    }




    public static void main(String[] args) {
        List list= new ArrayList<UserDO>();
        UserDO userDO = new UserDO("1","李粤",19,"男");
        UserDO userDO1 = new UserDO("2","李进",18,"男");
        list.add(userDO);
        list.add(userDO1);
        List list1 = BeansCopyUtils.listCopy(list, UserDTO.class);
        System.out.println(list1.toString());



    }
}
