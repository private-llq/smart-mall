package com.jsy.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yu
 * @since 2021-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_user_collect")
@ApiModel(value="UserCollect对象", description="")
public class UserCollect extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商家id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    @ApiModelProperty(value = "用户id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "商品id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long goodsId;

    @ApiModelProperty(value = "套餐id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long menuId;
    /**
     * 收藏类型：收藏类型：0 商品  1:服务  2：套餐  3：商店
     */
    private Integer type;

    /**
     * 店铺评分
     */
    private Double shopScore;
    /**
     * 店铺类型名称
     */
    private String shopTypeName;
    /**
     * 店铺联系电话
     */
    private String phone;
    /**
     * 商品/服务/套餐/店铺标题
     */
    private String title;
    /**
     * 商品/服务/套餐/店铺图片
     */
    private String image;
    /**
     * 商品/服务/套餐价格
     */
    private BigDecimal price;
    /**
     * 商品/服务/套餐折扣价格
     */
    private BigDecimal discountPrice;

    /**
     * 折扣状态
     */
   private Integer discountState;


    /**
     * 该条收藏记录的状态 true 正常  false 不正常
     */
    @TableField(exist = false)
    private Boolean state;





}
