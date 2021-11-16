package com.jsy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @className（类名称）: SelectUserOrderDTO
 * @description（类描述）: this is the SelectUserOrderDTO class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/16
 * @version（版本）: v1.0
 */
@Data
public class SelectUserOrderDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "c端用户id")
    private Long userId;

    @ApiModelProperty(value = "b端商家id")
    private Long shopId;

    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    @ApiModelProperty(value = "订单状态（[1待上门、待配送、待发货]，2、完成）")
    private Integer orderStatus;

    @ApiModelProperty(value = "支付方式（ 1app支付，2支付宝手机，3H5，4微信）")
    private Integer payType;

    @ApiModelProperty(value = "支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）")
    private Integer payStatus;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "账单月份")
    private Integer billMonth;

    @ApiModelProperty(value = "账单号")
    private String billNum;

    @ApiModelProperty(value = "账单抬头")
    private String billRise;

    @ApiModelProperty(value = "是否评价0未评价，1评价（评价完成为订单完成）")
    private Integer commentStatus;

    @ApiModelProperty(value = "订单类型（0-服务类(只有服务)，1-普通类（套餐，单品集合））")
    private Integer orderType;

    @ApiModelProperty(value = "预约状态（0预约中，1预约成功）")
    private Integer appointmentStatus;

    @ApiModelProperty(value = "消费方式（0用户到店，1商家上门）")
    private Integer consumptionWay;

    @ApiModelProperty(value = "订单的最终价格")
    private BigDecimal orderAllPrice;

    @ApiModelProperty(value = "用户配送地址id（针对商家上门）")
    private Long shippingAddress;

    @ApiModelProperty(value = "预计最早时间")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "预计最晚时间")
    private LocalDateTime entTime;


}
