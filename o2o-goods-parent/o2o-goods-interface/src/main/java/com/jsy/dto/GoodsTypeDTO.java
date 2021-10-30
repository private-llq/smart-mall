package com.jsy.dto;

import com.jsy.domain.GoodsBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("商品类型dto")
public class GoodsTypeDTO {
    /**
     * 商品类型id
     */
    @ApiModelProperty(name = "uuid",value = "商品类型uuid")
    private String uuid;

    @ApiModelProperty(name = "name",value = "类型名称")
    private String name;

    /**
     * 商品类型下所属商品
     */
    @ApiModelProperty(name = "basicList",value = "商品集合")
    private List<GoodsBasic> basicList;


}
