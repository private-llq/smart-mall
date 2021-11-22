package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.GoodsClientImpl;
import com.jsy.domain.Goods;
import com.jsy.dto.GoodsDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "SHOP-SERVICE-GOODS",fallback = GoodsClientImpl.class,configuration = FeignConfiguration.class)
public interface GoodsClient {
    @GetMapping("/goods/getShopIdGoods")
     CommonResult<Goods> getShopIdGoods(@RequestParam("shopId") Long shopId);

    /**
     * 最新上架 商品、服务、套餐
     * @param shopId
     * @return
     */
    @GetMapping("/goods/latelyGoods/{shopId}")
    CommonResult<Goods> latelyGoods(@PathVariable("shopId") Long shopId);


    /**
     * 批量查询 商品、服务
     */
    @PostMapping("/goods/batchGoods")
    CommonResult<List<GoodsDto>> batchGoods(@RequestBody List<Long> goodsList);

}
