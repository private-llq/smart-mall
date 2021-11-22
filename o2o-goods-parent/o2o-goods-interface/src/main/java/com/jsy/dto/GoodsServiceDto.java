package com.jsy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsServiceDto {

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

    @ApiModelProperty(value = "商品的作用")
    private String effect;

    @ApiModelProperty(value = "服务特点表ids 逗号隔开")
    private String serviceCharacteristicsIds;

    @ApiModelProperty(value = "服务时间1  星期+时间段")
    private String serviceTime1;

    @ApiModelProperty(value = "服务时间2  星期+时间段")
    private String serviceTime2;

    @ApiModelProperty(value = "服务的价格策略  0:输入价格，展示价格  1：不对价格作展示")
    private Integer priceStrategy;

    @ApiModelProperty(value = "文字介绍")
    private String textDescription;

    @ApiModelProperty(value = "图片介绍: 0-5张 以逗号分割")
    private String photoDescription;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "0:普通商品 1：服务类商品")
    private Integer type;

    /**
     * 是否支持上门服务：0 不支持 1 支持
     */
    private Integer isVisitingService;
}
