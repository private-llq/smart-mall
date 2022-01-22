package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.NewShop;
import com.jsy.dto.NewShopDto;
import com.jsy.dto.NewShopRecommendDto;
import com.jsy.query.NewShopQuery;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "shop-service-shop",configuration = FeignConfiguration.class)
public interface NewShopClient {
    /**
     * 根据id查询一条
     * @param shopId
     */
    //todo 网络http请求路径
    @GetMapping(value = "/newShop/get")
    CommonResult<NewShopDto> get(@RequestParam("shopId")Long shopId);


    @PostMapping("/newShop/batchIds")
    CommonResult<List<NewShopDto>> batchIds(@RequestBody List<Long> ids);

    @RequestMapping(value = "/getShopAllList",method = RequestMethod.POST)
    CommonResult<PageInfo<NewShopRecommendDto>> getShopAllList(@RequestBody NewShopQuery shopQuery);

    @ApiOperation("查询商家的imd")
    @RequestMapping(value = "/newShop/getShopImd",method = RequestMethod.POST)
    CommonResult<String> getShopImd(@RequestParam("shopId") Long shopId);

    @ApiOperation("查询商家所有信息")
    @RequestMapping(value = "/getById",method = RequestMethod.GET)
    CommonResult<NewShop> getById(@RequestParam("shopId") Long shopId);

    @ApiOperation("根据shopid查询userid")
    @GetMapping("/newShop/selectUseridByShopId")
    public CommonResult<Long> selectUseridByShopid(@RequestParam Long shopId);
}
