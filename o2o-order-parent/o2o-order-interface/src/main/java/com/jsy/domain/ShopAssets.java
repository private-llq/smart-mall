package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 商家资产
 * </p>
 *
 * @author yu
 * @since 2020-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_shop_assets")
public class ShopAssets implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺资产主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 资产uuid
     */
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
     * 银行卡账号uuid
     *          未绑定卡时为空
     */
    private String bankcardUuid;

    /**
     * 账号余额
     */

    private BigDecimal assets;

    private String walletId;

    private BigDecimal profits;

}
