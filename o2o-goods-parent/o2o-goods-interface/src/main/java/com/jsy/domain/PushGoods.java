package com.jsy.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2021-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_push_goods")
@ApiModel(value="PushGoods对象", description="")
public class PushGoods extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商家id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    @ApiModelProperty(value = "冗余字段 ：商店name")
    private String shopName;

    /**
     * 商品id
     */
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long goodsId;

    @ApiModelProperty(value = "商品/服务 分类id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long goodsTypeId;

    @ApiModelProperty(value = "冗余字段： 商品分类name")
    private String goodsTypeName;

    @ApiModelProperty(value = "商品编号")
    private String goodsNumber;

    @ApiModelProperty(value = "商品名称/服务标题")
    private String title;

    @ApiModelProperty(value = "商品/服务 - 图片1-3张")
    private String images;

    @ApiModelProperty(value = "商品/服务 价格")
    private BigDecimal price;

    /**
     * 折扣状态
     */
    private Integer discountState;

    @ApiModelProperty(value = "商品/服务 折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "商品的说明/服务的备注")
    private String textDescription;

    @ApiModelProperty(value = "排序字段")
    private Long sort;

    @ApiModelProperty(value = "销量")
    @TableField(exist = false)
    private Long sums=0L;

    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 维度
     */
    private BigDecimal latitude;


    /**
     * type 0:商品 1：服务 2：套餐 3：商店
     */
    private Integer type;

}
