package com.jsy.parameter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartParam implements Serializable {
    /**
     * 用户id
     */
    private String userId;

    /**
     * 商家id
     */
    private Long shopId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 套餐id
     */
    private Long setMenuId;

}
