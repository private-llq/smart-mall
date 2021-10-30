package com.jsy.dto;

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
public class UserRedpacketDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * uuid
     */
    @ApiModelProperty(value = "uuid" )
    private String uuid;

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
     * 红包大类(1店铺，2通用)
     */
    @ApiModelProperty(value = "红包大类(1店铺，2通用)" )
    private String typeName;

    /**
     * 商家店铺id
     */
    @ApiModelProperty(value = "商家店铺id" )
    private String shopUuid;


    /**
     * 商家店铺id
     */
    @ApiModelProperty(value = "商家名称" )
    private String shopName;

    /**
     * 金额
     */
    @ApiModelProperty(value = "金额" )
    private Integer money;


    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间" )
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime begintime;

    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间" )
    private LocalDateTime endtime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "使用状态" )
    private String useType;
}
