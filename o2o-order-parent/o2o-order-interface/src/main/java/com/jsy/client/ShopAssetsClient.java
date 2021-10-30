package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.vo.ShopAssetsVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "SERVICE-ORDER")
public interface ShopAssetsClient {
    @RequestMapping(value = "/shopAssets/saveAndUpdate", method = RequestMethod.POST)
    CommonResult save(@RequestBody ShopAssetsVO shopAssets);
}
