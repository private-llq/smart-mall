package com.jsy.dto;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoodsDto {

    /**
     * 商品的id
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "商家id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    /**
     * 商店名称
     */
    private String shopName;

    @ApiModelProperty(value = "商品/服务 - 图片1-3张")
    private String images;

    @ApiModelProperty(value = "商品/服务 分类id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long goodsTypeId;

    /**
     * 商品分类名称
     */
    private String goodsTypeName;

    @ApiModelProperty(value = "商品名称/服务标题")
    private String title;

    @ApiModelProperty(value = "上下架状态 0:下架 1：上架")
    private Integer isPutaway;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "是否开启折扣：0未开启 1开启")
    private Integer discountState;

    @ApiModelProperty(value = "商品折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "0:普通商品 1：服务类商品")
    private Integer type;

   /* @ApiModelProperty(value = "服务/商品 特点表ids 逗号隔开")
    private String serviceCharacteristicsIds;*/

    @ApiModelProperty(value = "商品的说明/服务的备注")
    private String textDescription;

   /* @ApiModelProperty(value = "是否支持上门服务 0 不支持 1 支持")
    private Integer isVisitingService;*/

    @ApiModelProperty(value = "销量")
    private Long sums=0L;

    /**
     * 访问量
     */
    private Long pvNum;

}
