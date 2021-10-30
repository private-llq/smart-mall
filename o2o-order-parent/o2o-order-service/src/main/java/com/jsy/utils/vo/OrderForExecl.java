package com.jsy.utils.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsy.basic.util.annotation.MapForExecl;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@TableName("t_order")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderForExecl {

    @MapForExecl(description = "id")
    private Long id;

    /**
     * 用户ID
     */
    @MapForExecl(description = "用户ID")
    private String userUuid;

    /**
     * 商家ID
     */
    @MapForExecl(description = "商家ID")
    private String shopUuid;

    /**
     * 创建时间
     */
    @MapForExecl(description = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @MapForExecl(description = "修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime updateTime;

    /**
     * 订单编号
     */
    @MapForExecl(description = "订单编号")
    private String orderNum;

    /**
     * 送达时间
     */
    @MapForExecl(description = "送达时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime serviceTime;

    /**
     * 订单状态
     */
    @MapForExecl(description = "订单状态")
    private Integer stateId;

    /**
     * 红包uuid
     */
    @MapForExecl(description = "红包uuid")
    private String redpacketUuid;

    /**
     * 配送费
     */
    @MapForExecl(description = "配送费")
    private Integer deliveryFee;

    /**
     * 订单价格
     */
    @MapForExecl(description = "订单价格")
    private BigDecimal orderPrice;

    /**
     * 订单原始价格
     */
    @MapForExecl(description = "订单原始价格")
    private BigDecimal orderOriginalPrice;


    /**
     * 用户评论信息
     */
    @MapForExecl(description = "用户评论信息")
    private Integer evaluationId;



    /**
     * 订单信息
     */
    @MapForExecl(description = "订单信息")
    private String orderMessage;

    /**
     * 用户名
     */
    @MapForExecl(description = "用户名")
    private String username;


    /**
     * 优惠了多少钱
     */
    @MapForExecl(description = "优惠了多少钱")
    private BigDecimal subtractPrice;
    /**
     * 用户电话
     */
    @MapForExecl(description = "用户电话")
    private String phone;

    /**
     * 用户地址
     */
    @MapForExecl(description = "用户地址")
    private String address;

    /**
     * 用户配送方式 1是商家配送，2是门店自提
     */
    @MapForExecl(description = "用户配送方式")
    private Integer deliveryWay;

    /**
     * uuid唯一标识
     */
    @MapForExecl(description = "uuid")
    private String uuid;
}
