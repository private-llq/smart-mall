package com.jsy.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
public class ShoppingCartListDto {
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

    @ApiModelProperty(value = "1：是套餐 0：不是套餐")
    private Integer isSetMenu;

    @ApiModelProperty(value = "1：支持上门 0：不支持上门")
    private Integer isVisitingService;
}
