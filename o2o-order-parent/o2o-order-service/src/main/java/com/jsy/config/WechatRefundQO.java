package com.jsy.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class WechatRefundQO implements Serializable {
    /**
     * 社区id
     */
    private Long communityId;

    /**
     * 退款单号
     */
    private String orderNum;

    /**
     * 交易来源 1.充值提现2.商城购物3.水电缴费4.物业管理5.房屋租金6.红包7.红包退回.8停车缴费.9房屋租赁
     */
    private Integer tradeFrom;

    /**
     * 其他服务关联id
     */
    private String serviceOrderNo;
}
