package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("修改店铺环境")
public class UpdateShopEnvironmentParam {
    @ApiModelProperty(name = "uuid", value = "店铺uuid")
    private String uuid;
    @ApiModelProperty(name = "shopImages1", value = "店铺环境图片1(file_url)")
    private String shopImages1;
    @ApiModelProperty(name = "shopImages2", value = "店铺环境图片2(file_url)")
    private String shopImages2;
    @ApiModelProperty(name = "shopImages3", value = "店铺环境图片3(file_url)")
    private String shopImages3;
}
