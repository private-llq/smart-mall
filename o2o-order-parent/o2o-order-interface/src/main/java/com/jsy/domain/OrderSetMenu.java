package com.jsy.domain;

import java.math.BigDecimal;
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
 * 订单的套餐表
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_order_set_menu")
@ApiModel(value="OrderSetMenu对象", description="订单的套餐表")
public class OrderSetMenu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "套餐名称")
    private String name;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "服务特点表ids 逗号隔开")
    private String serviceCharacteristicsIds;

    @ApiModelProperty(value = "原价")
    private BigDecimal realPrice;

    @ApiModelProperty(value = "折扣价")
    private BigDecimal sellingPrice;

    @ApiModelProperty(value = "图片（最大三张）")
    private String images;

    @ApiModelProperty(value = "开始有效期")
    private LocalDateTime indateStart;

    @ApiModelProperty(value = "截止有效期")
    private LocalDateTime indateEnd;

    @ApiModelProperty(value = "使用规则")
    private String rule;

    @ApiModelProperty(value = "套餐说明")
    private String menuExplain;




}
