package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("修改店铺经营时间")
public class UpdateNoticeParam {
    @ApiModelProperty(name = "uuid", value = "店铺uuid")
    private String uuid;
    @ApiModelProperty(name = "notice", value = "店铺公告")
    private String notice;
}
