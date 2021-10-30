package com.jsy.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;


@Data
@ApiModel("新增商品类型实体")
public class GoodsTypeVo implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 商品类型名称
     */
    @ApiModelProperty(value = "商品类型名称",name = "name")
    private String name;
    @ApiModelProperty(value = "分类描述",name = "details")
    private String details;

}
