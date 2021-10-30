package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("t_user_address")
public class UserAddress implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String uuid;
    /**
     * 用户id
     */
    private String userUuid;

    private String name;

    private String phone;

    /**
     * 收货地址
     */
    private String address;

    /**
     * 标签
     */
    private String tag;

    /**
     * 性别 0是男士1是女士
     */
    private String sex;

    /**
     * 详细地址
     */
    private String addressDescription;
    /**
     * 是否默认
     */
    private Integer isdefult;




}
