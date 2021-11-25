package com.jsy.client.impl;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.GoodsClient;
import com.jsy.domain.Goods;
import com.jsy.dto.GoodsDto;
import com.jsy.dto.GoodsServiceDto;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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

    @Override
    public CommonResult<List<GoodsDto>> batchGoods(List<Long> goodsList) {
        return null;
    }

    @Override
    public CommonResult<List<GoodsServiceDto>> batchGoodsService(List<Long> goodsServiceList) {
        return null;
    }
}
