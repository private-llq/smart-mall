package com.jsy.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("店铺筛选")
public class ShopScreen {

    @ApiModelProperty("送达时间")
    private int EstimatedTime;

    @ApiModelProperty("品质")
    private double star;



}
