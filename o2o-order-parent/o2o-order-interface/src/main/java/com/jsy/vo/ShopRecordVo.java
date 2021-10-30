package com.jsy.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ShopRecordVo {

    /**
     * 店铺uuid
     */
    private String uuid;

    /**
     * 金额流水
     */
    private BigDecimal record;

    /**
     * 操作类型0：提现，1：入账
     */
    private Integer turnType;

    /**
     * 流水账号
     */
    private String accountName;

    /**
     *
     */
    private String orderUuid;


}
