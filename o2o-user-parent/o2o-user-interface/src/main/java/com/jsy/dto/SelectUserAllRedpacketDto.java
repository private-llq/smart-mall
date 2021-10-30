package com.jsy.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@ApiModel("查询用户所有领取的红包")
public class SelectUserAllRedpacketDto implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "uuid" )
    private String uuid;

    @ApiModelProperty(value = "金额" )
    private Integer money;

    @ApiModelProperty(value = "红包大类(1店铺，2通用)" )
    private Integer type;

    @ApiModelProperty(value = "店铺名")
    private String shopName;

    @ApiModelProperty(value = "有效期" )
    private String validityTime;

    @ApiModelProperty(value = "关联的红包活动uuid" )
    private String activitieUuid;

    @ApiModelProperty(value = "1待使用 2已使用" )
    private Integer statesUS;
}
