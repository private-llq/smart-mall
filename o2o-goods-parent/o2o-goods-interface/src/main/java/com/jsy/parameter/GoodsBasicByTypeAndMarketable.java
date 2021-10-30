package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("根据商品分类uuid类型查询商品集合需要参数对象")
public class GoodsBasicByTypeAndMarketable {
    @ApiModelProperty(value = "shopUuid",name = "店铺uuid")
    private String shopUuid;
    @ApiModelProperty(value = "uuid类型",name = "typeUuid")
    private String typeUuid;
    @ApiModelProperty(value = "1上架0下架",name = "isMarketable")
    private String isMarketable;
}
