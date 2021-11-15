package com.jsy.client;


import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.*;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(value = "SHOP-SERVICE-SHOP",configuration = FeignConfiguration.class)
public interface NewShopClient {
    /**
     * 根据id查询一条
     * @param id
     */
    @GetMapping(value = "/newShop/get")
    CommonResult<NewShop> get (@RequestParam("id") Long id);




    }
