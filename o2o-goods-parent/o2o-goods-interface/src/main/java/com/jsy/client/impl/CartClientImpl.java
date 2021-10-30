package com.jsy.client.impl;
import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.CartClient;
import com.jsy.domain.Cart;
import com.jsy.query.CartQuery;
import com.jsy.vo.CartVo;

public class CartClientImpl implements CartClient {
    @Override
    public CommonResult addCart(Cart cart) {
        return  CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");
    }

    @Override
    public CommonResult queryCart(CartQuery CartQuery) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");
    }

    @Override
    public CommonResult queryCart2(CartQuery CartQuery) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");
    }

    @Override
    public CommonResult updateNum(Cart cart) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");
    }

    @Override
    public CommonResult deleteCart(String shopUuid, String goodsUuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");
    }

    @Override
    public CommonResult clearCart(String shop_uuid) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");
    }

    @Override
    public CommonResult BatchDeleteCart(CartVo cartVo) {
        return CommonResult.error(JSYError.FUSE_DOWNGRADE.getCode(),"熔断降级");
    }


}
