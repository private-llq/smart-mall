package com.jsy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("查询用户在店铺没有使用的红包")
public class SelectUserNoUserRedPacketDto {

    /**
     * uuid
     */
    @ApiModelProperty(value = "uuid")
    private String uuid;
    /**
     * 金额
     */
    @ApiModelProperty(value = "金额")
    private Integer money;
    /**
     * 获取时间
     */
    @ApiModelProperty(value = "获取时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime getTime;

    @ApiModelProperty(value = "到期时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueTime;
    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id")
    private String shopUuid;

    @ApiModelProperty(value = "有效期")
    private Integer validity;

    @ApiModelProperty(value = "状态(1可以使用0已过期)")
    private Integer statusA;


}
