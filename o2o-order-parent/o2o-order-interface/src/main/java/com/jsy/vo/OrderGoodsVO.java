package com.jsy.vo;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @className（类名称）: OrderGoodsVO
 * @description（类描述）: this is the OrderGoodsVO class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class OrderGoodsVO   {
    private Long id;


    private LocalDateTime createTime;

    @ApiModelProperty(value = "数量")
    private Integer amount;


    @ApiModelProperty(value = "商品名称")
    private String title;

    @ApiModelProperty(value = "商品 价格")
    private BigDecimal price;

    @ApiModelProperty(value = "商品 折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "商品 是否开启折扣：0未开启 1开启")
    private Integer discountState;

    @ApiModelProperty(value = "商品 - 图片1-3张")
    private String images;


    @ApiModelProperty(value = "商品id")
    private Long goodsId;
}
