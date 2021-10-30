package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("批量调整分类参数对象")
public class GoodsAdjustClassifyParam {
    @ApiModelProperty(value = "调整商品uuid数组",name = "goodUuidS")
    private List<String> goodUuidS;
    @ApiModelProperty(value = "分类id",name = "goods_type_uuid")
    private String goods_type_uuid;
}
