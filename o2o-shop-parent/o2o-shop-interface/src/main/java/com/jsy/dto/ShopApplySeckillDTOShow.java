package com.jsy.dto;

import com.jsy.basic.util.PageList;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Data
public class ShopApplySeckillDTOShow {
    /**
     * 店家设置活动开始时间戳 (秒)
     */
    private Long startTime;

    /**
     * 当前系统时间戳 （秒）
     */
    private Long systemTime=LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));


    /**
     * 分页集合
     */
    private PageList<ShopApplySeckillDTO> dtoPageList;

}
