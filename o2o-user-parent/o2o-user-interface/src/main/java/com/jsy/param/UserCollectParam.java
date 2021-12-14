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
     * 收藏类型：0 商品  1:服务  2：套餐  3：商店
     */
    private Integer type;

    /**
     * 收藏状态 true 收藏  false 未收藏
     */
    private Boolean state;


}
