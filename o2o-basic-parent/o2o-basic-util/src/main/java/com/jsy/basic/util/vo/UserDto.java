package com.jsy.basic.util.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;


@Data
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String uuid;

    /**
     * 姓名
     */
    private String name;


    /**
     * 电话号码
     */
    private String phone;


    /**
     * 用户类型 1普通用户 2店家账号 3平台管理员
     */
    private Integer userType;


    /**
     *关联Id 店家关联店铺Id (后期可关联用户详情表)
     */
    private String relationUuid;


    /**
     * 头像id
     */
    private String headSculpture;

}
