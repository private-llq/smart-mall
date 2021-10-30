package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2020-11-16
 */
@EqualsAndHashCode(callSuper = false)
@TableName("t_user_redpacket")
@ApiModel(description = "用户红包表")
@Data
public class UserRedpacket implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * uuid
     */
    @ApiModelProperty(value = "uuid" )
    private String uuid;
    /**
     * 金额
     */
    @ApiModelProperty(value = "金额" )
    private Integer money;
    /**
     * 获取时间
     */
    @ApiModelProperty(value = "获取时间")
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime getTime;
    /**
     * 使用时间
     */
    @ApiModelProperty(value = "使用时间" )
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime usedTime;
    /**
     * 是否有效
     */
    @ApiModelProperty(value = "调拨记录表id")
    private Integer deleted;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id" )
    private String userUuid;
    /**
     * 红包uuid
     */
    @ApiModelProperty(value = "红包uuid" )
    private String redpacketUuid;

    /**
     * 红包大类(1店铺，2通用)
     */
    @ApiModelProperty(value = "红包大类(1店铺，2通用)" )
    private Integer type;


    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopUuid;

    /**
     * 红包小类
     */
    @ApiModelProperty(value = "红包小类" )
    private Integer categoryType;

    @ApiModelProperty(value = "有效期" )
    private Integer validity;
    @ApiModelProperty(value = "关联的红包活动uuid" )
    private String activitieUuid;


}
