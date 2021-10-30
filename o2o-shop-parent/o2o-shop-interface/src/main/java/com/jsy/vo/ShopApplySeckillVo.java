package com.jsy.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShopApplySeckillVo {
    /**
     * uuid
     */
    private String uuid;
    /**
     * 商品数量
     */
    private Integer num;

    /**
     * 店家设置活动开始时间
     */
    private LocalDateTime startTime;

    /**
     * 店家设置活动结束时间
     */
    private LocalDateTime endTime;

    /**
     * 驳回原因
     */
    private String rejectionReasons;
}
