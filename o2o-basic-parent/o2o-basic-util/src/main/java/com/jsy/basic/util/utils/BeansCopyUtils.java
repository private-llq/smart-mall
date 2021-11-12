package com.jsy.basic.util.utils;

import cn.hutool.core.bean.BeanUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static cn.hutool.core.bean.BeanUtil.copyProperties;

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
            copyProperties(source, target);
            return target;
        }).collect(Collectors.toList());
    }
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target) {
        return copyListProperties(sources, target, null);
    }

    /**
     * <p>带回调参数的集合数据的拷贝</p>
     *
     * @param sources  数据源类
     * @param target   目标类::new (eg: UserVO::new)
     * @param callBack 回调函数 eg: BeanCopyUtil.copyListProperties(userLiset,User:: new, (userDo,userVo) ->{
     *                 userVo.setSex()})
     * @return List<T> List<Bean>
     * @author renkaijiang
     * @Date 2020/6/19 15:09
     */
    public static <S, T> List<T> copyListProperties(List<S> sources, Supplier<T> target, BeanCopyUtilCallBack<S, T> callBack) {
        List<T> list = new ArrayList<>(sources.size());
        for (S source : sources) {
            T t = target.get();
            copyProperties(source, t);
            list.add(t);
            if (callBack != null) {
                callBack.callBack(source, t);
            }
        }
        return list;
    }
    /**
     * @Description 默认回调方法
     * @ClassName BeanCopyUtilCallBack
     * @Author renkaijiang
     * @Date 2020/6/19 14:22
     * @Version 1.0
     */
    public interface BeanCopyUtilCallBack<S, T> {
        void callBack(S s, T t);
    }

 /**
  //原数据
  List<UserEntity> ue = userService.getUser();

  //不需要特殊处理，所以不写回调方法
  List<UserVO> user = BeanCopyUtil.copyListProperties(ue,UserVO::new);


  //有参数不一致或者需要处理的，例如UserVO的name = UserEntity的userName，
  //状态需要转化
  List<UserVO> user = BeanCopyUtil.copyListProperties(ue,UserVO::new,
  (userEntity,userVO) ->{
  userVO.setName(userEntity.getUserName());

  userVo.setStatus(userEntity.getStatus==1?"正常":"锁定");



  });


  */



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
