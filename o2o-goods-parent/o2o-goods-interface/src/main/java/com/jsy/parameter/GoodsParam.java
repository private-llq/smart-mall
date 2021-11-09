package com.jsy.parameter;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsParam {

    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "商品图片1-5张/服务宣传图、视频")
    private String images;

    @ApiModelProperty(value = "商品分类id(服务分类)")
    private Long goodsTypeId;

    @ApiModelProperty(value = "商品名称/服务标题")
    private String title;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "文字介绍")
    private String textDescription;

    @ApiModelProperty(value = "图片介绍: 0-5张 以逗号分割")
    private String photoDescription;

}
