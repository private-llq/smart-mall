package com.jsy.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;

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
 * @since 2021-11-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_shopping_cart")
@ApiModel(value="ShoppingCart对象", description="")
public class ShoppingCart extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品图片1-3张/服务宣传图、视频")
    private String images;

    @ApiModelProperty(value = "商品名称/服务标题")
    private String title;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "是否开启折扣：0未开启 1开启")
    private Integer discountState;

    @ApiModelProperty(value = "商品折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "购物车数量")
    private Integer num;





}
