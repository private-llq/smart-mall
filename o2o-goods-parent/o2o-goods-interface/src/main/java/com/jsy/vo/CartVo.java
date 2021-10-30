package com.jsy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "Cart",description = "购物车vo")
public class CartVo {
    /**
     * 商店id
     */
    @ApiModelProperty(value = "商店id")
    private String shopUuid;
    /**
     * 商品ids
     */
    @ApiModelProperty(value = "商品ids字符串")
    private String ids;
}
