package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.NewShop;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "shop-service-shop",configuration = FeignConfiguration.class)
public interface NewShopClient {
    /**
     * 根据id查询一条
     * @param id
     */
    //todo 网络http请求路径
    @GetMapping(value = "/newShop/get/")
    CommonResult<NewShop> get(@PathVariable("id")Long id);

}
