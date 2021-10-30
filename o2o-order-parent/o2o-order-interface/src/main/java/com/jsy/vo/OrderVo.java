package com.jsy.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderVo implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 前端验证幂等性
     */
    private String uuid;
    /**
     * 店铺uuid1
     */
    private String shopUuid;

    /**
     * 用户红包uuid1
     */
    private String userRedpacket;

    /**
     * 地址uuid1
     */
    private String addressUuid;

    private String userUuid;

    /**
     * 订单编号
     */
    private String orderNum;

    /**
     * 订单状态
     */
    private Integer stateId;

    /**
     * 配送费1
     */
    private Integer deliveryFee;

    /**
     * 用户评论信息
     */
    private Integer evaluationId;

    /**
     * 订单信息1
     */
    private String orderMessage;

    /**
     * 派送方式或自提1
     */
    private Integer deliveryWay;

    /**
     * 是否使用
     */
    private String used;

    private String serviceCode;

    private String shopSent;
}
