package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品添加中添加的属性对象")
public class GoodsPropertyParam {
    @ApiModelProperty(value = "商品的属性名",name = "propertyName")
    private String propertyName;
    @ApiModelProperty(value = "商品的属性的细分类",name = "propertyClass")
    private String propertyClass;
}
