package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2020-11-20
 */
@EqualsAndHashCode(callSuper = false)
@TableName("t_platform_activity")
@Data
public class PlatformActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "平台活动uuid",name = "uuid")
    private String uuid;
    /**
     * 管理者
     */
    private Long managerUuid;

    /**
     * 满足的上线金额
     */
    private Integer erverySum;

    /**
     * 减免金额
     */
    private Integer reduceNum;

    /**
     * 活动名称
     */
    private String name;

    /**
     * 是否有效 0:无效 1:有效
     */
    private Integer deleted;

    /**
     * 开始时间
     */
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

}
