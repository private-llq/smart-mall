package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.ShopInfo;
import com.jsy.dto.ShopMessageDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@FeignClient(value = "SERVICE-SHOP",configuration = FeignConfiguration.class)
public interface ShopInfoClient {

    @ApiOperation("根据店铺uid获取店铺信息")
    @GetMapping("/shopInfo/pub/{uuid}")
    public CommonResult getByRelationUid(@PathVariable("uuid")String uuid);

    @ApiOperation("查询店铺信息")
    @RequestMapping(value="/shopInfo/selectShopMessage",method= RequestMethod.GET)
    public CommonResult<ShopMessageDto> selectShopMessage(@RequestParam("shopUuid") String shopUuid);

    @ApiOperation("[用户]根据uuid查看店铺信息")
    @RequestMapping(value = "/shopInfo/pub/get/{uuid}",method = RequestMethod.GET)
    public CommonResult<ShopInfo> get(@PathVariable("uuid")String uuid);

    @ApiOperation("查询商家单笔配送费")
    @RequestMapping(value="/shopInfo/selectPostage",method= RequestMethod.GET)
    public CommonResult<BigDecimal> selectPostage(@RequestParam("shopUuid") String shopUuid);
}
