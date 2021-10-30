package com.jsy.domain;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * </p>
 * @author lijin
 * @since 2020-11-23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(callSuper = false)
@TableName("t_cart")
@ApiModel(value = "Cart",description = "购物车实体类")
public class Cart implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 购物车id
     */
    @ApiModelProperty(value = "购物车id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 购物车uuid
     */
    @ApiModelProperty(value = "购物车uuid")
    private String uuid;


    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户uuid")
    private String userUuid;

    /**
     * 商店id
     */
    @ApiModelProperty(value = "商店uuid")
    private String shopUuid;
    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品uuid")
    private String goodsUuid;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
    private String title;

    /**
     * 图片
     */
    @ApiModelProperty(value = "图片")
    private String image;

    /**
     * 价格
     */
    @ApiModelProperty(value = "价格")
    private BigDecimal price;

    /**
     * 商品规格参数(描述)
     */
    @ApiModelProperty(value = "商品规格参数(描述)")
    @TableField("ownSpec")
    private String ownSpec;

    /**
     *
     * 购物车每行商品的单价*数量
     */
    @TableField(exist = false)
    private BigDecimal sumCartPriceShow=BigDecimal.ZERO;



    /**
     * 秒杀商品标识：1 表示秒杀商品 0 表示普通商品
     */
    private Integer killGoods=0;

    /**
     * 秒杀价格 秒杀商品特有属性
     */
    private BigDecimal killPrice;

    /**
     * 秒杀开始时间 秒杀商品特有属性
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    //@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime killStartTime;

    /**
     * 秒杀商品状态 1：过时 0：未过时 秒杀商品特有属性
     */
    private Integer killState;
    /**
     * 购物数量 秒杀商品特有属性
     */
    @ApiModelProperty(value = "购物数量")
    private Integer num;


    /**
     * 折扣价格
     */
    private BigDecimal discountPrice;
    /**
     * 商品的折扣(0.10-0.99)
     */
    private Double discount;
    /**
     * 折扣开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    //@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime discountStartTime;
    /**
     * 折扣结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    //@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime discountEndTime;
    /**
     * 1按照折扣0按照折扣价格
     */
    private Integer discountStatus;
    /**
     * 折扣活动状态(1启用0未启动)
     */
    private Integer activityStatus;


}
