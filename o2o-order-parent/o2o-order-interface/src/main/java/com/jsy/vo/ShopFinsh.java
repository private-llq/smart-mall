package com.jsy.vo;

import lombok.Data;


@Data
public class ShopFinsh {
    /**
     * 店家uuid
     */
    private String uuid;
    /**
     * 订单uuid
     */
    private String orderUuid;
    /**
     * 商家到达位置
     */
    private String end;

    /**
     * 用户uuid
     */
    private String userUuid;
}
