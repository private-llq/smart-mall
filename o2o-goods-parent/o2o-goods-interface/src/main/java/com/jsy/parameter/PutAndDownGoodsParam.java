package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;

@Data
@ApiModel("批量上架下架参数对象")
public class PutAndDownGoodsParam {
    @ApiModelProperty(value = "上架数组",name = "goodUuidS")
    private List<String> goodUuidS;
    @ApiModelProperty(value = " 1上架/0下架",name = "isMarketable")
    private String isMarketable;
}
