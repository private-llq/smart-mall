package com.jsy.parameter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("修改店铺营业时间")
public class UpdateShopBusinessTimeParam implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "uuid",value = "营业时间uuid" )
    private String uuid;
    @ApiModelProperty(name = "startTime", value = "开始营业时间")
    private String startTime;
    @ApiModelProperty(name = "endTime", value = "结束营业时间")
    private String endTime;
    @ApiModelProperty(name = "status", value = "状态")
    private Integer status;
}
