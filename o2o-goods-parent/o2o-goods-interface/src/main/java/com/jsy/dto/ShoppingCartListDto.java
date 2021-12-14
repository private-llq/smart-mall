package com.jsy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ShoppingCartListDto {

    @ApiModelProperty(value = "id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long Id;
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

    @ApiModelProperty(value = "是否开启折扣：0未开启 1开启")
    private Integer discountState;

    @ApiModelProperty(value = "商品折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "购物车数量")
    private Integer num;

    /**
     * 该商品状态：true 正常 false 不正常（大后台禁用、商家下架）
     */

    private Boolean state;


    @ApiModelProperty(value = "0：商品 1：服务  2：套餐")
    private Integer Type;


}
