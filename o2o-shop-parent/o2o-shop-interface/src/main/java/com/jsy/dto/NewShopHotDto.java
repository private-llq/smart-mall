package com.jsy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewShopHotDto implements Serializable {
    @ApiModelProperty(value = "商品id")
    private Long id;
    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "商品/服务 - 图片1-3张")
    private String images;

    @ApiModelProperty(value = "商品名称/服务标题")
    private String title;

    @ApiModelProperty(value = "商品折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "访问量")
    private Long pvNum;
    @ApiModelProperty(value = "类型  类型2套餐   服务和商品是0")
    private Integer type;


}
