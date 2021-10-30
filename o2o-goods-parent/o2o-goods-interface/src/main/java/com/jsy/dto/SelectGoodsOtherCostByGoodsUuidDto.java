package com.jsy.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@Data
@ApiModel("根据商品的uuid查询其他收费项目")
public class SelectGoodsOtherCostByGoodsUuidDto {
    //
    public  String name;
    public BigDecimal price;
}
