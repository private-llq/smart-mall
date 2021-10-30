package com.jsy.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class NewUserDto {
    /**
     * 昨日新客
     */
    private Integer yesterdayNewUse;
    /**
     * 今日新客
     */
    private Integer todayNewUser;
    /**
     * 累计新客
     */
    private Integer CumulativeNewUser;
    /**
     * 累计成本
     */
    private BigDecimal CumulativePrice;
    /**
     *
     * 立减金额
     */
    private BigDecimal price;

    /**
     * 开始时间
     */
    private Long startTime;

    /**
     * 结束时间
     */
    private Long endTime;
    /**
     * 剩余天数
     */
    private Double surplusDay;
    /**
     * 持续天数
     */
    private Double durationDay;
    /**
     * 是否进行中 1 :进行中 0：未进行
     */
    private Integer isConduct;
    /**
     * 活动创建时间 返回时间戳
     */
    private Long creatTime;
    /**
     * 活动撤销时间 返回时间戳
     */
    private Long revokeTime;
}
