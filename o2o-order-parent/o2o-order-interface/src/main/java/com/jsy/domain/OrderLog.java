package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2020-12-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_order_log")
public class OrderLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 日志描述
     */
    private String description;

    /**
     * 方法
     */
    private String method;

    /**
     * 类型
     */
    private String type;

    /**
     * 请求ip
     */
    private String requestIp;

    /**
     * 异常code
     */
    private String exceptionCode;

    /**
     * 异常描述
     */
    private String exceptionDetail;

    /**
     * 参数
     */
    private String params;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 触发人
     */
    private String person;

    /**
     * 状态
     */
    private Integer stateId = 0;

    /**
     * 解决人反馈
     */
    private String finishDescription;

    /**
     * 处理人
     */
    private String finishPersonUuid = "smart china";
}
