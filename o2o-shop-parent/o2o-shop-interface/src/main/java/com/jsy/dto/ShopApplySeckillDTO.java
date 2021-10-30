package com.jsy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class ShopApplySeckillDTO {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 商品uuid
     */
    private String goodsUuid;
    /**
     * 商品name
     */
    private String goodsName;
    /**
     * 商品价格
     */
    private BigDecimal price;
    /**
     * 商品折扣价格
     */
    private BigDecimal discountPrice;
    /**
     * 商品秒杀价格
     */
    private BigDecimal seckillPrice;
    /**
     * 商品图片
     */
    private String imagesUrl;
    /**
     * 商店uuid
     */
    private String shopUuid;
    /**
     * 商店name
     */
    private String shopName;
    /**
     * 商家UserName
     */
    private String shopUserName;
    /**
     * 商店地址
     */
    private String addressDetail;
    /**
     * 每人限购数量
     */
    private Integer purchaseRestrictions;
    /**
     * 商家电话
     */
    private String phone;
    /**
     * 商品数量
     */
    private Integer num;
    /**
     * 店家设置活动开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private LocalDateTime startTime;

    private Long startTimes;
    /**
     * 店家设置活动结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",locale = "zh",timezone = "GMT+8")
    private LocalDateTime endTime;

    private Long endTimes;

    /**
     * 当前系统时间戳
     */
    private Long systemTime=LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));







}
