package com.jsy.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class UserCollectParam implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "套餐id")
    private Long menuId;

    /**
     * 收藏类型：0 商品、服务 1：套餐 2：商店
     */
    private Integer type;


}
