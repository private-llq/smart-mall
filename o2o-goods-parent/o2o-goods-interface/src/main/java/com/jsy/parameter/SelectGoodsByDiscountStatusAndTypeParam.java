package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("根据商品类型和折扣状态查询商品信息参数对象")
public class SelectGoodsByDiscountStatusAndTypeParam {
    @ApiModelProperty(value = "shopUuid",name = "店铺uuid")
    private String shopUuid;
    @ApiModelProperty(value = "uuid类型",name = "typeUuid")
    private String typeUuid;
    @ApiModelProperty(value = "商品活动的状态（0没参加1进行中2已失效）",name = "status")
    private String status;
}
