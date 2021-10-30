package com.jsy.domain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author lijin
 * @since 2020-11-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_record")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 抢购表主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * uuid
     */
    private String uuid;
    /**
     * 抢购的商品
     */
    private String goodsUuid;

    /**
     * 抢到的用户
     */
    private String userUuid;

    /**
     * 商店的主键
     */
    private String shopUuid;

    /**
     * 限购
     */
    private Integer xianGou;
    /**
     * 已购
     */
    private Integer yiGod;
    /**
     * 原价
     */
    private BigDecimal price;
    /**
     * 秒杀价
     */
    private BigDecimal seckillPrice;
    /**
     * 商品数量
     */
    private Integer goods_num;




}
