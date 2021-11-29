package com.jsy.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class BackstageGoodsParam implements Serializable {

    private Long id;

    @ApiModelProperty(value = "商品分类id")
    private Long goodsTypeId;

    @ApiModelProperty(value = "商品标题")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "产品编号")
    private String goodsNumber;

    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "商品图片")
    private String images;

    @ApiModelProperty(value = "详细内容")
    private String detail;

    @ApiModelProperty(value = "品牌")
    private BigDecimal brand;

    @ApiModelProperty(value = "展示价格")
    private BigDecimal showPrice;

    @ApiModelProperty(value = "市场价格")
    private BigDecimal marketPrice;

}
