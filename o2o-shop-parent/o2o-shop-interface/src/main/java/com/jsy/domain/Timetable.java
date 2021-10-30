package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author yu
 * @since 2021-02-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_timetable")
@NoArgsConstructor
@AllArgsConstructor
public class Timetable implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String uuid;

    private LocalTime time;
    /**
     * 使用状态 0:未使用，1：使用中，2：结束时间
     */
    private Integer state;

    /**
     * 拼接带天数的活动时长
     */
    private LocalDateTime endTimeLong;


}
