package com.jsy.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;


@Data
public class UserAddressVO implements Serializable {
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
     * 详细地址
     */
    private String addressDescription;
    /**
     * 性别 0是男士1是女士
     */
    private String sex;
    /**
     * 标签
     */
    private String tag;
    /**
     * 是否默认
     */
    private Integer isdefult;



}
