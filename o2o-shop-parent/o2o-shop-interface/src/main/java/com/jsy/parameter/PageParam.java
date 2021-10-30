package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("Page参数")
@Data
public class PageParam {
    @ApiModelProperty(name = "current", value = "当前页数")
    private  Integer current;
    @ApiModelProperty(name = "size", value = "每页条数")
    private  Integer size;
}
