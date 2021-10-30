package com.jsy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("折扣商品返回对象")
public class SelectJoinActivityGoodsDto {
    @ApiModelProperty(name = "uuid",value = "商品uuid")
    private String uuid;
    @ApiModelProperty(value = "店铺uuid",name = "shopUuid")
    private String shopUuid;
    @ApiModelProperty(value = "商品价格",name = "price")
    private BigDecimal price;

    @ApiModelProperty(value = "商品标题",name = "title")
    private String title;
    @ApiModelProperty(value = "商品编号",name = "goodNumber")
    private String goodNumber;
    @ApiModelProperty(value = "商品图片地址",name = "imagesUrl")
    private String imagesUrl;
    @ApiModelProperty(value = "商品销量",name = "sales")
    private Integer sales;
    @ApiModelProperty(value = "商品折扣价格",name = "discountPrice")
    private BigDecimal discountPrice;
    @ApiModelProperty(value = "商品的折扣(0.10-0.99)",name = "discount")
    private Double discount;
    @ApiModelProperty(value = "1按照折扣比例0按照折扣价格",name = "discountStatus")
    private Integer discountStatus;
    @ApiModelProperty(value = "限购份数",name = "astrictNumber")
    private Integer astrictNumber;
    @ApiModelProperty(value = "折扣开始时间",name = "discountStartTime")
    private LocalDateTime discountStartTime;
    @ApiModelProperty(value = "折扣结束时间",name = "discountEndTime")
    private LocalDateTime discountEndTime;
    @ApiModelProperty(value = "折扣活动状态",name = "activityStatus")
    private Integer activityStatus;
}
