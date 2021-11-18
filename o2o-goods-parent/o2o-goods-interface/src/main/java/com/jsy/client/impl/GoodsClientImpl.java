package com.jsy.client.impl;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.GoodsClient;
import com.jsy.domain.Goods;
import org.springframework.web.bind.annotation.GetMapping;

public class GoodsClientImpl implements GoodsClient {
    @Override
    public CommonResult<Goods> getShopIdGoods(Long shopId) {
        return null;
    }

    @GetMapping("/goods/latelyGoods/{shopId}")
    @Override
    public CommonResult<Goods> latelyGoods(Long shopId) {
        return null;
    }
}
