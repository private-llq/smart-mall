package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品顺序排列参数对象")
public class GoodRankParam {
    /**
     * 商品uuid
     */
    @ApiModelProperty(name = "uuid",value = "商品uuid")
    private String uuid;

    /**
     * 排序序号
     */
    @ApiModelProperty(value = "排序序号",name = "ranks")
    private Integer ranks;
}
