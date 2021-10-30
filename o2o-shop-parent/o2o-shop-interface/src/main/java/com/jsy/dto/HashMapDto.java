package com.jsy.dto;

import lombok.Data;

import java.time.LocalTime;
import java.util.List;
import java.util.TreeMap;

@Data
public class HashMapDto {
    /**
     * 开始时间段 时间戳
     */
    private Long time;
    /**
     * 这批活动总结结束时间 时间戳
     */
    private Long endTime;
    /**
     * 活动信息数据
     */
    private TreeMap<LocalTime, List<ShopApplySeckillDTO>> data;
}
