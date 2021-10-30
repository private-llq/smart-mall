package com.jsy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Time;

@Data
@ApiModel("查询出店铺营业时间")
public class SelectShopBusinessTimeDto {
    @ApiModelProperty(name = "uuid",value = "营业时间uuid" )
    private String uuid;
    @ApiModelProperty(name = "week", value = "星期")
    private  Integer  week;
    @ApiModelProperty(name = "startTime", value = "开始营业时间")
    private Time startTime;
    @ApiModelProperty(name = "endTime", value = "结束营业时间")
    private Time endTime;
    @ApiModelProperty(name = "status", value = "状态1正常使用营业时间，0全天休息")
    private Integer status;
}
