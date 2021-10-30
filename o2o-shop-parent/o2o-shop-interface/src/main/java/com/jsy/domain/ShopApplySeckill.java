package com.jsy.domain;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author yu
 * @since 2020-12-19
 */

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_shop_apply_seckill")
public class ShopApplySeckill implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 商店uuid
     */
    private String shopUuid;

    /**
     * 商品uuid
     */
    private String goodsUuid;

    /**
     * 参加活动的商品数量
     */
    private Integer num;

    /**
     * 秒杀价格
     */
    private BigDecimal seckillPrice;

    /**
     * 每人限购
     */
    private Integer purchaseRestrictions;

    /**
     * 店家设置活动开始时间
     */
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 店家设置活动结束时间
     */
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    /**
     * 	0：已驳回 1:已通过 2：申请中
     */
    private Integer state;


}
