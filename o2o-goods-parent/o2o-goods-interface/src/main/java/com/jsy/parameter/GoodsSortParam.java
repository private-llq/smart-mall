package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;



@Data
@ApiModel("接受商品分类排序的顺序")
public class GoodsSortParam implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "商品类型uuid",name = "uuid")
    private String uuid;
    @ApiModelProperty(value = "排序序号",name = "ranks")
    private Integer ranks;
}
