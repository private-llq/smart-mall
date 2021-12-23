package com.jsy.config;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @program: com.jsy.community
 * @description: 微信支付订单
 * @author: Hu
 * @create: 2021-01-25 10:06
 **/
@ApiModel("微信支付订单记录表")
@Data
@TableName("t_wechat_order")
public class WeChatOrderEntity implements Serializable {
    @ApiModelProperty(value = "订单号")
    private String id;
    @ApiModelProperty(value = "物业公司id")
    private Long companyId;
    @ApiModelProperty(value = "微信支付订单号")
    private String transactionId;
    @ApiModelProperty(value = "用户id")
    private String uid;
    @ApiModelProperty(value = "交易来源 1.充值提现2.商城购物3.水电缴费4.物业管理5.房屋租金6.红包7.红包退回.8停车缴费.9房屋租赁")
    private Integer payType;
    @ApiModelProperty(value = "商品描述")
    private String description;
    @ApiModelProperty(value = "其他业务id")
    private String serviceOrderNo;
    @ApiModelProperty(value = "支付金额")
    private BigDecimal amount;
    @ApiModelProperty(value = "订单状态，1未付款，2已付款，3已退款")
    private Integer orderStatus;
    @ApiModelProperty(value = "到账状态，1未到账，2已到账，3已退款")
    private Integer arriveStatus;
    @ApiModelProperty(value = "退款单号")
    private String refundNum;
    @ApiModelProperty(value = "退款时间")
    private LocalDateTime refundTime;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
}
