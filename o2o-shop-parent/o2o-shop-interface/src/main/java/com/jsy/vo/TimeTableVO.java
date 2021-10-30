package com.jsy.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TimeTableVO {
    private static final long serialVersionUID = 1L;

    private String uuid;

    private LocalTime time;
    /**
     * 使用状态 0:未使用，1：使用中，2：结束时间
     */
    private Integer state;
}
