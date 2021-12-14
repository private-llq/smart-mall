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

@ApiModel("商品返回对象")
public class GoodsBasicDto implements Serializable {

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
     * 商品状态0:失效；1:正常
     */
    @ApiModelProperty(value = "商品状态",name = "status",notes = "0:正常;1:失效")
    private String status;
    /**
     * 商品详情
     */
    @ApiModelProperty(value = "商品详情",name = "ownSpec")
    private String ownSpec;


    /**
     * 是否上架0:否；1:上架
     */
    @ApiModelProperty(value = "商品是否上架",name = "isMarketable",notes = "0:否;1:上架")
    private String isMarketable;
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
     * 是否启用规格0:否；1:启用
     */
    @ApiModelProperty(value = "是否启用规格",name = "isEnableSpec",notes = "0:不启用;1:启用")
    private String isEnableSpec;

    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片地址",name = "imagesUrl")
    private String imagesUrl;

    /**
     * 库存
     */
    @ApiModelProperty(value = "商品库存",name = "stock")
    private Integer stock;

    /**
     * 商品销量
     */
    @ApiModelProperty(value = "商品销量",name = "sales")
    private Integer sales;




}
