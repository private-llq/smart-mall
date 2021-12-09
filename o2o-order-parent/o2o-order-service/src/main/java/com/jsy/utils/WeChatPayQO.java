package com.jsy.utils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class WeChatPayQO {
    @ApiModelProperty(value = "交易来源 1.充值提现2.商城购物3.水电缴费4.物业管理5.房屋租金6.红包7.红包退回.8停车缴费.9房屋租赁，10临时缴费")
    private Integer tradeFrom;
    @ApiModelProperty(value = "支付描述")
    private String descriptionStr;
    @ApiModelProperty("支付金额")
    private BigDecimal amount;
    @ApiModelProperty("商城订单")
    private Map<String,Object> orderData;

    @ApiModelProperty("订单id集合")
    private String ids;

    @ApiModelProperty("其他服务id")
    private String serviceOrderNo;

    @ApiModelProperty("停车缴费临时记录id")
    private Long communityId;


}