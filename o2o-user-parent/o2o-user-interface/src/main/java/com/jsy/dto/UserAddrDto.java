package com.jsy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserAddrDto implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "联系人")
    private String linkman;

    @ApiModelProperty(value = "性别    0  男  |  1 女")
    private Integer sex;

    @ApiModelProperty(value = "电话")
    private String pone;

    @ApiModelProperty(value = "地区")
    private String district;

    @ApiModelProperty(value = "详细地址")
    private String detailedAddress;

    @ApiModelProperty(value = "标签 0 家 | 1 公司 | 2 学校 ")
    private String tag;

    @ApiModelProperty(value = "默认地址 1 是 0 否")
    private Integer defaultAddress;
}
