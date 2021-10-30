package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2020-12-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_shop_record")
public class ShopRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 资产账号uuid
     */
    private String assetsUuid;

    /**
     * 金额流水
     */
    private BigDecimal record;

    /**
     * 操作类型0：提现，1：入账
     */
    private Integer turnType;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 到账时间
     */
    private LocalDateTime arrivalTime;

    /**
     * 流水单号
     */
    private String accountNumber;

    /**
     * 流水账号用户uuid
     */
    private String accountName;

    /**
     * 订单编号
     */
    private String orderUuid;
    /**
     * 状态：1有效，2无效
     */
    private Integer stateId;

}
