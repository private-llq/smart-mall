package com.jsy.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户地址管理表
 * </p>
 *
 * @author yu
 * @since 2021-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_user_addr")
@ApiModel(value="UserAddr对象", description="用户地址管理表")
public class UserAddr extends BaseEntity implements Serializable  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userId;

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
