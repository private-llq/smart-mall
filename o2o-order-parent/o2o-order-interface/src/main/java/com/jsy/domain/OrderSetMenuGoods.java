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
 * 订单的套餐详情表
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_order_set_menu_goods")
@ApiModel(value="OrderSetMenuGoods对象", description="订单的套餐详情表")
public class OrderSetMenuGoods extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "订单套餐id（外键）")
    private Long orderMenuId;

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

    @ApiModelProperty(value = "服务特点表ids 逗号隔开")
    private String serviceCharacteristicsIds;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;



}
