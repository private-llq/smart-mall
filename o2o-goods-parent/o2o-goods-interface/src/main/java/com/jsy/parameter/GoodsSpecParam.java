package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
@Data
@ApiModel("商品添加中添加的规格对象")
public class GoodsSpecParam {
    @ApiModelProperty(value = "规格名称",name = "specName")
    private String specName;
    @ApiModelProperty(value = "规格价格",name = "specPrice")
    private BigDecimal specPrice;
}
