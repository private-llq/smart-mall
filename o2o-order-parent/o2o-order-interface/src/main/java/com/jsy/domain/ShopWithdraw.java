package com.jsy.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.time.LocalDate;
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
 * 商家提现记录表
 * </p>
 *
 * @author arli
 * @since 2021-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_shop_withdraw")
@ApiModel(value="ShopWithdraw对象", description="商家提现记录表")
public class ShopWithdraw  extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "金额")
    private BigDecimal money;

    @ApiModelProperty(value = "提现单据号")
    private String withdrowNumber;

    @ApiModelProperty(value = "提现方式0微信1支付宝")
    private Integer withdrowWay;

    @ApiModelProperty(value = "创建时间")
    private LocalDate createDate;

}
