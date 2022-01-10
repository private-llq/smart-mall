package com.jsy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @className（类名称）: SelectShopOrderDto
 * @description（类描述）: this is the SelectShopOrderDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/29
 * @version（版本）: v1.0
 */
@Data
public class SelectShopOrderDto extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    @ApiModelProperty(value = "c端用户id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long userId;

    @ApiModelProperty(value = "b端商家id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    @ApiModelProperty(value = "订单编号")
    private String orderNum;
    @ApiModelProperty(value = "订单状态显示数据")
    private String orderStatusShow;
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
    @ApiModelProperty(value = "退款角色0商家1是平台")
    private Integer refundApplyRole;
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
    @ApiModelProperty(value = "商家地址（用户到店）")
    private String shippingAddress;
    @ApiModelProperty(value = "联系人")
    private String linkman;
    @ApiModelProperty(value = "性别    0  男  |  1 女")
    private Integer sex;
    @ApiModelProperty(value = "电话")
    private String telepone;
    @ApiModelProperty(value = "地区")
    private String district;
    @ApiModelProperty(value = "详细地址")
    private String detailedAddress;
    @ApiModelProperty(value = "验证状态")
    private Integer serverCodeStatus;
    @ApiModelProperty(value = "预计最早时间")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "预计最晚时间")
    private LocalDateTime entTime;
    @ApiModelProperty(value = "如果是商品的添加到商品详情")
    private List<SelectUserOrderGoodsDto> orderGoodsDtos=new ArrayList<>();
    @ApiModelProperty(value = "如果是服务的添加到服务详情")
    private   List<SelectUserOrderServiceDto> orderServiceDtos=new ArrayList<>();
    @ApiModelProperty(value = "如果是套餐的添加到套餐详情")
    private  List<SelectUserOrderMenuDto> orderMenuDtos=new ArrayList<>();
}
