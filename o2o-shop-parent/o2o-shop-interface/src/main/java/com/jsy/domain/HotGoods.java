package com.jsy.domain;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 热门商品表
 * </p>
 *
 * @author yu
 * @since 2021-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_hot_goods")
@ApiModel(value="HotGoods对象", description="热门商品表")
public class HotGoods extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "商家id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    @ApiModelProperty(value = "商品/服务 - 图片1-3张")
    private String images;

    @ApiModelProperty(value = "商品名称/服务标题")
    private String title;

    @ApiModelProperty(value = "商品折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "访问量")
    private Long pvNum;


    @ApiModelProperty(value = "商品id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long goodsId;
    @ApiModelProperty(value = "类型 类型2套餐   服务和商品是0 ")
    private Integer type;


}
