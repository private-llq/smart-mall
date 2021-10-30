package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.sql.Time;


@EqualsAndHashCode(callSuper = false)
@TableName("t_business_hours")
@ApiModel(description = "营业时间表")
@Data
public class BusinessHours implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value ="id",type=IdType.AUTO)
    private Long id;
    @ApiModelProperty(name = "uuid",value = "营业时间uuid" )
    private String uuid;
    @ApiModelProperty(name = "shopUuid", value = "店铺uuid")
    private  String shopUuid;
    @ApiModelProperty(name = "week", value = "星期")
    private  Integer  week;
    @ApiModelProperty(name = "startTime", value = "开始营业时间")
    private Time startTime;
    @ApiModelProperty(name = "endTime", value = "结束营业时间")
    private Time endTime;
    @ApiModelProperty(name = "status", value = "状态")
    private Integer status;
}
