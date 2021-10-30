package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@ApiModel("折扣商品参数对象")
public class SelectJoinActivityGoodsParam {

    @ApiModelProperty(value = "店铺uuid",name = "shopUuid")
    private String shopUuid;
    @ApiModelProperty(name = "1(进行中)0(已结束)",value = "status")
    private Integer status;
    @ApiModelProperty(name = "1(安装折扣排序)0(按照结束时间排序)",value = "ranks")
    private Integer ranks;
}
