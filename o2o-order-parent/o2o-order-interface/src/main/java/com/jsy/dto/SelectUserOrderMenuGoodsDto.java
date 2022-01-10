package com.jsy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @className（类名称）: SelectUserOrderMenuGoodsDto
 * @description（类描述）: this is the SelectUserOrderMenuGoodsDto class
 * @author（创建人）: ${Administrator}
 * @createDate（创建时间）: 2021/11/16
 * @version（版本）: v1.0
 */
@Data
public class SelectUserOrderMenuGoodsDto {
    @ApiModelProperty(value = "订单套餐id（外键）")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long orderMenuId;

    @ApiModelProperty(value = "数量")
    private Integer amount;

    @ApiModelProperty(value = "商家id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    @ApiModelProperty(value = "商品名称")
    private String title;

    @ApiModelProperty(value = "商品 价格")
    private BigDecimal price;

//    @ApiModelProperty(value = "商品 折扣价格")
//    private BigDecimal discountPrice;
//
//    @ApiModelProperty(value = "商品的说明")
//    private String textDescription;

//    @ApiModelProperty(value = "商品分类id")
//    private Long goodsTypeId;

//    @ApiModelProperty(value = "商品 是否开启折扣：0未开启 1开启")
//    private Integer discountState;

//    @ApiModelProperty(value = "商品/服务 - 图片1-3张")
//    private String images;

    @ApiModelProperty(value = "商品id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long goodsId;


}
