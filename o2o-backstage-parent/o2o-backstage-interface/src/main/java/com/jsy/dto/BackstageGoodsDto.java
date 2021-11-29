package com.jsy.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BackstageGoodsDto implements Serializable {

    @ApiModelProperty(value = "商品标题")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "展示价格")
    private BigDecimal showPrice;


}
