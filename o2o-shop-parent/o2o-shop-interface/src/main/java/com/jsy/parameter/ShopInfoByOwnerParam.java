package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("拥有者店铺列表")
@Data
public class ShopInfoByOwnerParam {
    @ApiModelProperty(name = "uuid", value = "店铺uuid")
    private String uuid;
    @ApiModelProperty(name = "shopName", value = "店铺名称")
    private String shopName;
    @ApiModelProperty(name = "status", value = "店铺状态（关于审核）")
    private Integer status;
    @ApiModelProperty(name = "businessStatus", value = "营业状态(1营业中，2休息中，3暂停营业)")
    private Integer businessStatus;
    @ApiModelProperty(name = "addressDetail", value = "店铺详细地址）")
    private String addressDetail;
    @ApiModelProperty(name = "shopLogo", value = "店铺logo图片(file_url)")
    private String shopLogo;
    @ApiModelProperty(name = "updateTime", value = "店铺修改时间")
    private LocalDateTime updateTime;
}
