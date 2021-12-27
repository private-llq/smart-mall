package com.jsy.vo;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @className（类名称）: OrderSetMenuVO
 * @description（类描述）: this is the OrderSetMenuVO class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class OrderSetMenuVO  {

    private Long id;


    private LocalDateTime createTime;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "套餐id")
    private Long menuId;

    @ApiModelProperty(value = "套餐名称")
    private String name;



    @ApiModelProperty(value = "原价")
    private BigDecimal realPrice;

    @ApiModelProperty(value = "折扣价")
    private BigDecimal sellingPrice;

    @ApiModelProperty(value = "折扣状态0未开启1是开启")
    private Integer discountState;


    @ApiModelProperty(value = "图片（最大三张）")
    private String images;



    @ApiModelProperty(value = "数量")
    private Integer amount;
}
