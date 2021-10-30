package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2020-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;



    /**
     * uuid主键
     */
    private String uuid;
    /**
     * 姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 电话号码
     */
    private String phone;



    /**
     *关联Id 店家关联店铺Id (后期可关联用户详情表)
     */
     private String relationUuid;


    /**
     * 头像id
     */
    private String headSculpture;


}
