package com.jsy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel(description = "用户领取红包")
@Data
public class ReceiveRedpacketVo {

    /**
     * 红包大类(1店铺，2通用)
     */
    @ApiModelProperty(value = "红包大类(1店铺，2通用)" )
    @NotNull(message = "红包类型不能为空")
    private Integer type;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "红包Id")
    @NotNull(message = "红包Id不能为空")
    private String redpacketUuid;
}
