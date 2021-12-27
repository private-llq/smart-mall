package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.math.BigDecimal;
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
 * 商家余额表
 * </p>
 *
 * @author arli
 * @since 2021-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_shop_capital")
@ApiModel(value="ShopCapital对象", description="商家余额表")
public class ShopCapital  extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "余额")
    private BigDecimal capital;




}
