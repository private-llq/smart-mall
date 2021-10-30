package com.jsy.parameter;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jsy.domain.GoodsOtherCost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@Data
@ApiModel("商品添加的参数对象")
public class GoodsBasicParam implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "商品图片地址用;隔开",name = "imagesUrl")
    private String imagesUrl;
    @ApiModelProperty(value = "商品标题",name = "title")
    private String title;
    @ApiModelProperty(value = "店铺uuid",name = "shopUuid")
    private String shopUuid;
    @ApiModelProperty(value = "商品类型uuid",name = "goodsTypeUuid")
    private String goodsTypeUuid;
    @ApiModelProperty(value = "商品编号",name = "goodNumber")
    private String goodNumber;
    @ApiModelProperty(value = "商品价格",name = "price")
    private BigDecimal price;
    @ApiModelProperty(value = "商品详情",name = "ownSpec")
    private String ownSpec;
    @ApiModelProperty(value = "商品折扣价格",name = "discountPrice")
    private BigDecimal discountPrice;
    @ApiModelProperty(value = "商品规格集合",name = "GoodsSpecParams")
    private List<GoodsSpecParam> GoodsSpecParams;
    @ApiModelProperty(value = "商品属性集合",name = "GoodsPropertyParams")
    private List<GoodsPropertyParam> GoodsPropertyParams;
    @ApiModelProperty(value = "商品其他收费集合",name = "GoodsOtherCostParams")
    private List<GoodsOtherCostParam> GoodsOtherCostParams;
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
