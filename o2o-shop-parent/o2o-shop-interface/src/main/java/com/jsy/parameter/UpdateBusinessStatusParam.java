package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("修改营业状态参数列表")
public class UpdateBusinessStatusParam {
    @ApiModelProperty(name = "uuid", value = "店铺uuid")
    private String uuid;
    @ApiModelProperty(name = "businessStatus", value = "营业状态1营业中，2休息中，3暂停营业")
    private Integer businessStatus;
}
