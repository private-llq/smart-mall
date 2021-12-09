package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.GoodsClientImpl;
import com.jsy.client.impl.SetMenuClientImpl;
import com.jsy.domain.HotGoods;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "shop-service-shop",fallback = GoodsClientImpl.class,configuration = FeignConfiguration.class)
public interface HotClient {
    @ApiOperation("根据商品id彻底删除热门数据表")
    @RequestMapping(value = "/hotGoods/delHotGoods",method = RequestMethod.POST)
    CommonResult<Boolean> delHotGoods(@RequestParam("goodsId") Long goodsId);

    @ApiOperation("根据店铺id彻底删除热门数据表")
    @RequestMapping(value = "/hotGoods/delHotShop",method = RequestMethod.POST)
    CommonResult<Boolean> delHotShop(@RequestParam("shopId") Long shopId);

    @ApiOperation("热根据店铺id查询是否是门数据")
    @RequestMapping(value = "/hotGoods/getHotShop",method = RequestMethod.POST)
    CommonResult<HotGoods> getHotShop(@RequestParam("shopId") Long shopId);

    @ApiOperation("根据商品id查询是否是热门数据")
    @RequestMapping(value = "/hotGoods/getHotGoods",method = RequestMethod.POST)
    CommonResult<HotGoods> getHotGoods(@RequestParam("goodsId") Long goodsId);

    @ApiOperation("查询所有热门数据")
    @RequestMapping(value = "/hotGoods/getHotList",method = RequestMethod.POST)
    CommonResult<List<HotGoods>> getHotList();
}
