package com.jsy.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ActivityDTO {
    /**
     * 昨日订单
     */
    private Integer yesterdayOrder;
    /**
     * 今日订单
     */
    private Integer todayOrder;
    /**
     * 累计订单
     */
    private Integer CumulativeOrder;
    /**
     * 累计减免
     */
    private BigDecimal CumulativePrice;

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
     * 是否进行中 1 :进行中 0：撤销
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
