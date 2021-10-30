package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("修改接单电话")
public class UpdateOrderReceivingPhoneParam {
    @ApiModelProperty(name = "uuid", value = "店铺uuid")
    private String uuid;
    @ApiModelProperty(name = "orderReceivingPhone", value = "接单电话最多三个用;隔开")
    private  String orderReceivingPhone;
}
