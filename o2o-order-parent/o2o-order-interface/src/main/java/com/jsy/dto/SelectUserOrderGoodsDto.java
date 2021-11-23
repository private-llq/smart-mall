package com.jsy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @className（类名称）: selectUserOrderGoodsDto
 * @description（类描述）: this is the selectUserOrderGoodsDto class
 * @author（创建人）: ${Administrator}
 * @createDate（创建时间）: 2021/11/16
 * @version（版本）: v1.0
 */
@Data
public class SelectUserOrderGoodsDto {
    @ApiModelProperty(value = "订单id（外键）")
    private Long orderId;

    @ApiModelProperty(value = "数量")
    private Integer amount;

    @ApiModelProperty(value = "商家id")
    private Long shopId;

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

    @ApiModelProperty(value = "服务特点表集合")
    private List<ServiceCharacteristicsDto> ServiceCharacteristicsDtos;
}
