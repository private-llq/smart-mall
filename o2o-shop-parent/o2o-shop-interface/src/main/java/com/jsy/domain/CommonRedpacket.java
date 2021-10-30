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
 * @since 2020-11-20
 */
@EqualsAndHashCode(callSuper = false)
@TableName("t_common_redpacket")
@ApiModel(description = "通用红包")
@Data
public class CommonRedpacket implements Serializable {

    private static final long serialVersionUID = 1L;




    /**
     * uuid主键
     */
    @ApiModelProperty(value = "店铺红包uuid" )
    private String uuid;

    /**
     * 管理者的id
     */
    @ApiModelProperty(value = "管理者的uuid")
    private String managerUuid;

    /**
     * 红包金额
     */
    @ApiModelProperty(value = "红包金额")
    private Integer money;

    /**
     * 数量
     */
    @ApiModelProperty(value = "数量")
    private Integer num;

    /**
     * 开始时间
     */
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private LocalDateTime begintime;

    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8",pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endtime;

    /**
     * 红包类型
     */
    @ApiModelProperty(value = "红包类型")
    private Integer type;

    /**
     * 是否有效
     */
    @ApiModelProperty(value = "是否有效")
    private Integer deleted;
}
