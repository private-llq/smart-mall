package com.jsy.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2021-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_user_browse")
@ApiModel(value="Browse对象", description="用户个人最近浏览记录")
public class Browse extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "商品、服务、套餐名称")
    private String name;

    @ApiModelProperty(value = "说明")
    private String textDescription;

    @ApiModelProperty(value = "原价")
    private BigDecimal realPrice;

    @ApiModelProperty(value = "折扣价")
    private BigDecimal sellingPrice;

//    @ApiModelProperty(value = "是否支持上门服务   0不支持  1支持")
//    private Integer isVisitingService;

    @ApiModelProperty(value = "0 商品  1:服务  2：套餐   3：商店")
    private Integer type;
    @ApiModelProperty(value = "是否开启折扣：0未开启 1开启")
    private Integer discountState;

    @ApiModelProperty(value = "图片（最大三张）")
    private String images;



}
