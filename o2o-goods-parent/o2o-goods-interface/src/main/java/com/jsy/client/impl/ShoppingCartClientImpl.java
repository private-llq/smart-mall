package com.jsy.client.impl;

import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.ShoppingCartClient;
import com.jsy.dto.ShoppingCartDto;
import com.jsy.parameter.ShoppingCartParam;

public class ShoppingCartClientImpl implements ShoppingCartClient {
    @Override
    public CommonResult addShoppingCart(ShoppingCartParam shoppingCartParam) {
        return null;
    }

    @Override
    public CommonResult addSetMenu(ShoppingCartParam shoppingCartParam) {
        return null;
    }

    @Override
    public CommonResult clearCart(ShoppingCartParam shoppingCartParam) {
        return null;
    }

    @Override
    public CommonResult delShoppingCart(Long id) {
        return null;
    }

    @Override
    public CommonResult reduceShoppingCart(Long id) {
        return null;
    }

    @Override
    public CommonResult<ShoppingCartDto> queryCart(ShoppingCartParam shoppingCartParam) {
        return null;
    }

    @Override
    public CommonResult<PageInfo<ShoppingCartDto>> queryCartAll(ShoppingCartParam shoppingCartParam) {
        return null;
    }
}
