package com.jsy.dto;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "查询店铺红包参数信息对象")
@Data
public class SelectShopRedpacketDto {

    /**
     * uuid主键
     */
    @ApiModelProperty(value = "店铺红包uuid" )
    private String uuid;

    /**
     * 商家店铺id
     */
    @ApiModelProperty(value = "商家店铺id" )
    private String shopUuid;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额" )
    private Integer money;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量" )
    private Integer num;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间" )
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime begintime;

    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间" )
    private LocalDateTime endtime;
    /**
     * 类型
     */
    @ApiModelProperty(value = "类型" )
    private Integer type;

    /**
     * 随机红包最小金额
     */
    @ApiModelProperty(value = "随机红包最小金额" )
    private BigDecimal min;

    /**
     * 随机红包最大金额
     */
    @ApiModelProperty(value = "随机红包最小金额" )
    private BigDecimal  max;
    /**
     * 间隔0（每人限领一次）1（每天可以领一个）
     */
    @ApiModelProperty(value = "间隔0（每人限领一次）1（每天可以领一个）" )
    private Integer  intervals;

    /**
     * 状态0进行中，1撤销
     */
    @ApiModelProperty(value = "状态0进行中，1撤销" )
    private Integer status;

    /**
     * 剩余时间
     */
    @ApiModelProperty(value = "剩余时间（小时）" )
    private Integer residue;

    /**
     * 有效期时间
     */
    @ApiModelProperty(value = "有效期时间（小时）" )
    private Integer valid;
    /**
     * 累计领取数量
     */
    @ApiModelProperty(value = "累计领取数量" )
    private Integer getNumber;
    /**
     * 使用数量
     */
    @ApiModelProperty(value = "使用数量" )
    private Integer useNumber;
    /**
     * 未使用金额
     */
    @ApiModelProperty(value = "未使用金额" )
    private Integer noMoney;
    /**
     * 已经使用金额
     */
    @ApiModelProperty(value = "已经使用金额" )
    private Integer yesMoney;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间" )
    private LocalDateTime created;
    /**
     * 红包有效期
     */
    @ApiModelProperty(value = "红包有效期（天）" )
    private Integer validity;
}
