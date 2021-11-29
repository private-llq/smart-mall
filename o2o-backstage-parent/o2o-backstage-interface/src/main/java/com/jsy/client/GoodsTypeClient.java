package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.TreeClientImpl;
import com.jsy.dto.GoodsTypeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(value = "shop-service-backstage",fallback = TreeClientImpl.class,configuration = FeignConfiguration.class)
public interface GoodsTypeClient {

    /**
     * 根据id查询一条
     * @param id
     */
    @GetMapping(value = "/industryCategory/{id}")
    CommonResult<GoodsTypeDto> get(@PathVariable("id")Long id);
}
