package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.domain.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "SHOP-SERVICE-SHOP",configuration = FeignConfiguration.class)
public interface NewShopClient {
    /**
     * 根据id查询一条
     * @param id
     */
    @GetMapping(value = "/newShop/get")
    NewShop get (@RequestParam("id") Long id);




    }
