package com.jsy.dto;

import com.jsy.domain.GoodsOtherCost;
import com.jsy.domain.GoodsProperty;
import com.jsy.domain.GoodsSpec;
import com.jsy.domain.GoodsType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@ApiModel("商品返回对象")
public class GoodsSelectDetailsDTO {

    @ApiModelProperty(name = "uuid",value = "商品uuid")
    private String uuid;
    @ApiModelProperty(value = "店铺uuid",name = "shopUuid")
    private String shopUuid;
    @ApiModelProperty(value = "商品价格",name = "price")
    private BigDecimal price;
    @ApiModelProperty(value = "商品折扣价格",name = "discountPrice")
    private BigDecimal discountPrice;
    @ApiModelProperty(value = "商品状态",name = "status",notes = "0:正常;1:失效")
    private String status;
    @ApiModelProperty(value = "商品详情",name = "ownSpec")
    private String ownSpec;
    @ApiModelProperty(value = "商品是否上架",name = "isMarketable",notes = "0:否;1:上架")
    private String isMarketable;
    @ApiModelProperty(value = "商品标题",name = "title")
    private String title;
    @ApiModelProperty(value = "商品类型uuid",name = "goodsTypeUuid")
    private String goodsTypeUuid;
    @ApiModelProperty(value = "商品类型",name = "goodsType")
    private String goodsType;
    @ApiModelProperty(value = "商品编号",name = "goodNumber")
    private String goodNumber;
    @ApiModelProperty(value = "排序序号",name = "ranks")
    private Integer ranks;
    @ApiModelProperty(value = "是否启用规格",name = "isEnableSpec",notes = "0:不启用;1:启用")
    private String isEnableSpec;
    @ApiModelProperty(value = "商品图片地址",name = "imagesUrl")
    private String imagesUrl;
    @ApiModelProperty(value = "商品库存",name = "stock")
    private Integer stock;
    @ApiModelProperty(value = "商品销量",name = "sales")
    private Integer sales;
    @ApiModelProperty(value = "商品规格集合",name = "goodsSpecs")
    private ArrayList<GoodsSpec>  goodsSpecs;
    @ApiModelProperty(value = "商品其他费用集合",name = "GoodsOtherCosts")
    private ArrayList<GoodsOtherCost>  goodsOtherCosts;
    @ApiModelProperty(value = "商品属性集合",name = "goodsPropertys")
    private ArrayList<GoodsProperty>  goodsPropertys;
    @ApiModelProperty(value = "商品的折扣(0.10-0.99)",name = "discount")
    private Double discount;
    @ApiModelProperty(value = "折扣开始时间",name = "discountStartTime")
    private LocalDateTime discountStartTime;
    @ApiModelProperty(value = "折扣结束时间",name = "discountEndTime")
    private LocalDateTime discountEndTime;
    @ApiModelProperty(value = "折扣结束时间",name = "astrictNumber")
    private Integer astrictNumber;
    @ApiModelProperty(value = "折扣活动状态",name = "activityStatus")
    private Integer activityStatus;
    @ApiModelProperty(value = "1按照折扣比例0按照折扣价格",name = "discountStatus")
    private Integer discountStatus;

}
