package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("商品添加中添加的其他收费对象")
public class GoodsOtherCostParam {

    @ApiModelProperty(value = "商品其他收费名称",name = "name")
    private String name;

    @ApiModelProperty(value = "商品其他收费金额",name = "price")
    private BigDecimal price;
}
