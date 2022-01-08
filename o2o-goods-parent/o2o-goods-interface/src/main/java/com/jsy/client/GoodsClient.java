package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.GoodsClientImpl;
import com.jsy.domain.Goods;
import com.jsy.dto.GoodsDto;
import com.jsy.dto.GoodsServiceDto;
import com.jsy.query.NearTheServiceQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "shop-service-goods",fallback = GoodsClientImpl.class,configuration = FeignConfiguration.class)
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
     * 批量查询 商品
     */
    @PostMapping("/goods/batchGoods")
    CommonResult<List<GoodsDto>> batchGoods(@RequestBody List<Long> goodsList);

    /**
     * 批量查询 服务
     */
    @PostMapping("/goods/batchGoodsService")
    CommonResult<List<GoodsServiceDto>> batchGoodsService(@RequestBody List<Long> goodsServiceList);


    /**
     * 查看一条商品的详细信息 B端+C端
     * @param id
     * @return
     */
    @GetMapping("/goods/getGoods")
    CommonResult<GoodsDto> getGoods(@RequestParam("id") Long id);


    /**
     * 查看一条服务的所有详细信息 B端+C端
     * @param id
     *
     */
    @GetMapping("/goods/getGoodsService")
    CommonResult<GoodsServiceDto> getGoodsService(@RequestParam("id") Long id);


    /**
     * 查看一条商品或者服务的所有详细信息 B端+C端
     * @param id
     *
     */
    @ApiOperation("查看一条商品或者服务的详细信息")
    @GetMapping("/goods/getByGoods")
   CommonResult<Goods> getByGoods(@RequestParam("id") Long id);
    /**
     * 医疗端：附近的服务2
     */
    @PostMapping("/goods/NearTheService2")
    CommonResult<List<GoodsServiceDto>> NearTheService2(@RequestBody NearTheServiceQuery nearTheServiceQuery);

    /**
     * 商家被禁用，同步禁用商家的商品和服务
     * type 0 禁用  1 取消
     */
    @GetMapping("/goods/disableAll")
    CommonResult disableAll(@RequestParam("shopId") Long shopId,@RequestParam("type") Integer type);

    /**
     * 统计店家商品的发布数量 (type=0:商品 1：服务1)
     */
    @GetMapping("/goods/getGoodsNumber")
    CommonResult<Integer> getGoodsNumber(@RequestParam("shopId") Long shopId,@RequestParam("type") Integer type);
}
