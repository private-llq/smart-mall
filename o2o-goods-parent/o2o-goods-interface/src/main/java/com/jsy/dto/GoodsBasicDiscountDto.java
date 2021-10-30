package com.jsy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@Data

@ApiModel("按分类和折扣状态返回对象")
public class GoodsBasicDiscountDto implements Serializable {

    private static final long serialVersionUID = 1L;



    /**
     * 商品uuid
     */
    @ApiModelProperty(name = "uuid",value = "商品uuid")
    private String uuid;

    /**
     * 商品类型id
     */
    @ApiModelProperty(value = "商品类型uuid",name = "goodsTypeUuid")
    private String goodsTypeUuid;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺uuid",name = "shopUuid")
    private String shopUuid;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格",name = "price")
    private BigDecimal price;

    @ApiModelProperty(value = "商品折扣价格",name = "discountPrice")
    private BigDecimal discountPrice;

    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品标题",name = "title")
    private String title;
    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号",name = "goodNumber")
    private String goodNumber;
    /**
     * 排序序号
     */
    @ApiModelProperty(value = "排序序号",name = "ranks")
    private Integer ranks;
    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片地址",name = "imagesUrl")
    private String imagesUrl;

    /**
     * 商品销量
     */
    @ApiModelProperty(value = "商品销量",name = "sales")
    private Integer sales;




}
