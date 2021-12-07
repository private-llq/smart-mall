package com.jsy.query;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @className（类名称）: CreationOrderMenuGoodsParam
 * @description（类描述）: this is the CreationOrderMenuGoodsParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/15
 * @version（版本）: v1.0
 */
@Data
public class CreationOrderMenuGoodsParam implements Serializable  {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "数量")
    private Integer amount;



    @ApiModelProperty(value = "商品分类id")
    private Long goodsTypeId;

    @ApiModelProperty(value = "商品名称")
    private String title;

    @ApiModelProperty(value = "商品/服务 价格")
    private BigDecimal price;

    @ApiModelProperty(value = "商品 折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "商品的说明")
    private String textDescription;

    @ApiModelProperty(value = "商品 是否开启折扣：0未开启 1开启")
    private Integer discountState;

    @ApiModelProperty(value = "商品/服务 - 图片1-3张")
    private String images;


    @ApiModelProperty(value = "商品id")
    private Long goodsId;
}
