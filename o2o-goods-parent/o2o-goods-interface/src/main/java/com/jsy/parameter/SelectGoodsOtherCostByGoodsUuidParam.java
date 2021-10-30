package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("根据商品的uuid查询其他收费项目参数对象")
public class SelectGoodsOtherCostByGoodsUuidParam {
    public  String goodUuid;
    public  Integer num;
}
