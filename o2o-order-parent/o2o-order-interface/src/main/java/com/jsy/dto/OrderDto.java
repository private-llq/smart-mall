package com.jsy.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = false)
@TableName("t_order")
@Data
public class OrderDto implements Serializable {


    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 用户UUID
     */
    private String userUuid;
    /**
     * 商家ID
     */
    private String shopUuid;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime updateTime;
    /**
     * 订单编号
     */
    private String orderNum;
    /**
     * 优惠了多少钱
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal subtractPrice;
    /**
     * 送达时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime serviceTime;
    /**
     * 订单状态
     */
    private Integer stateId;
    /**
     * 红包uuid
     */
    private String redpacketUuid;
    /**
     * 配送费
     */
    private Integer deliveryFee;
    /**
     * 订单价格
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal orderPrice;
    /**
     * 订单价格
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal orderOriginalPrice;
    /**
     * 用户评论信息
     */
    private Integer evaluationId;
    /**
     * 订单信息
     */
    private String orderMessage;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户电话
     */
    private String phone;
    /**
     * 用户地址
     */
    private String address;
    /**
     * 用户配送方式 1是商家配送，2是门店自提
     */
    private Integer deliveryWay;
    /**
     * 支付状态
     */
    private String payState;
    /**
     * uuid唯一标识
     */
    private String uuid;
    /**
     * 是否使用
     */
    private String used;
    private String serviceCode;
    private String shopSent;
    @ApiModelProperty(value = "其他费用")
    private String otherMoney;

    @TableField(exist = false)
    private List<OrderCommodityDto> orderCommodityDtos = new ArrayList<>();


}
