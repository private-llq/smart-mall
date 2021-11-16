package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.CartClientImpl;
import com.jsy.client.impl.GoodsClientImpl;
import com.jsy.domain.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "SHOP-SERVICE-GOODS",fallback = GoodsClientImpl.class,configuration = FeignConfiguration.class)
public interface GoodsClient {
    @GetMapping("/goods/getShopIdGoods")
    CommonResult<Goods> getShopIdGoods(@RequestParam("shopId") Long shopId);
}
