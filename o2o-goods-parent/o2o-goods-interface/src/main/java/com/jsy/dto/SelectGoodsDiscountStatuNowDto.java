package com.jsy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("查询商品当前是否是折扣商品返回对象")
public class SelectGoodsDiscountStatuNowDto {


    /**
     * 是否是折扣商品
     */
    private Boolean aBoolean;
    /**
     * 商品折扣价格
     */
    @ApiModelProperty(value = "商品价格",name = "price")
    private BigDecimal price;
}
