package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
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
@ApiModel("商品修改的参数对象")
public class GoodsBasicUpdateParam implements Serializable {

    @ApiModelProperty(name = "uuid",value = "商品uuid")
    private String uuid;
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "商品图片地址用;隔开",name = "imagesUrl")
    private String imagesUrl;
    @ApiModelProperty(value = "商品标题",name = "title")
    private String title;
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
    private List<GoodsSpecUpdateParam> GoodsSpecParams;
    @ApiModelProperty(value = "商品属性集合",name = "GoodsPropertyParams")
    private List<GoodsPropertyUpdateParam> GoodsPropertyParams;
    @ApiModelProperty(value = "商品其他收费集合",name = "GoodsOtherCostParams")
    private List<GoodsOtherCostUpdateParam> GoodsOtherCostParams;






}
