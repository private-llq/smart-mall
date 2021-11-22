package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.NewShop;
import com.jsy.dto.NewShopDto;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "shop-service-shop",configuration = FeignConfiguration.class)
public interface NewShopClient {
    /**
     * 根据id查询一条
     * @param id
     */
    //todo 网络http请求路径
    @GetMapping(value = "/newShop/get/{id}")
    CommonResult<NewShop> get(@PathVariable("id")Long id);


    @PostMapping("/newShop/batchIds")
    CommonResult<List<NewShopDto>> batchIds(@RequestBody List<Long> ids);


}
