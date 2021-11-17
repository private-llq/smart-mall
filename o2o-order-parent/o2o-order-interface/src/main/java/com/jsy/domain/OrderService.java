package com.jsy.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 订单的服务表
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_order_service")
@ApiModel(value="OrderService对象", description="订单的服务表")
public class OrderService  extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;



    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "服务 - 图片1-3张")
    private String images;

    @ApiModelProperty(value = "服务分类id(服务分类)")
    private Long goodsTypeId;

    @ApiModelProperty(value = "服务标题")
    private String title;

    @ApiModelProperty(value = "服务价格")
    private BigDecimal price;

    @ApiModelProperty(value = "折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "是否开启折扣：0未开启 1开启")
    private Integer discountState;

    @ApiModelProperty(value = "服务特点表ids 逗号隔开")
    private String serviceCharacteristicsIds;

    @ApiModelProperty(value = "服务的备注")
    private String textDescription;

    @ApiModelProperty(value = "服务电话")
    private String phone;

    @ApiModelProperty(value = "服务的使用规则")
    private String serviceRegulations;

    @ApiModelProperty(value = "服务有效期")
    private LocalDateTime validUntilTime;

    @ApiModelProperty(value = "服务数量")
    private Integer amount;




}
