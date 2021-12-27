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
 * 商家账单表
 * </p>
 *
 * @author arli
 * @since 2021-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_shop_bill")
@ApiModel(value="ShopBill对象", description="商家账单表")
public class ShopBill  extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;



    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "订单号")
    private String orderNumber;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "说明")
    private String explains;

    @ApiModelProperty(value = "月份")
    private Integer month;

    @ApiModelProperty(value = "日")
    private Integer day;

    @ApiModelProperty(value = "年份")
    private Integer year;

    @ApiModelProperty(value = "0收入1退款")
    private Integer billType;

    @ApiModelProperty(value = "金额")
    private BigDecimal money;


}
