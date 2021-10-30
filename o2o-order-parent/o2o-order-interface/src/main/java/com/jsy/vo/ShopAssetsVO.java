package com.jsy.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("新增资产信息vo")
@Data
public class ShopAssetsVO implements Serializable {

    private String uuid;

    /**
     * 用户账号uuid
     */
    private String ownerUuid;

    /**
     * 支付宝账号
     */
    private String alipay;

    /**
     * 微信账号
     */
    private String wechatpaty;

    /**
     * 银行卡账号
     */
    private String bankcardUuid;

    /**
     * 账号余额
     */
    private BigDecimal assets;
}
