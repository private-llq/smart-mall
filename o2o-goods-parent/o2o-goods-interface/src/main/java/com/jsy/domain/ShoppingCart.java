package com.jsy.domain;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2021-11-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_shopping_cart")
@ApiModel(value="ShoppingCart对象", description="")
public class ShoppingCart extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "用户id")
    private Long userId;

    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "套餐id")
    private Long setMenuId;

    @ApiModelProperty(value = "商品图片1-3张/服务宣传图、视频")
    private String images;

    @ApiModelProperty(value = "商品名称/服务标题")
    private String title;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "商品折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "购物车数量")
    private Integer num;

    @ApiModelProperty(value = "是否开启折扣：0未开启 1开启")
    private Integer discountState;

    @ApiModelProperty(value = "0:商品 1：服务 2：套餐")
    private Integer type;

  /*  @ApiModelProperty(value = "是否支持上门服务 1：支持 0： 不支持")
    private Integer isVisitingService;*/




}
