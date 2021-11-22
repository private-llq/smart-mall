package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.CartClientImpl;
import com.jsy.client.impl.SetMenuClientImpl;
import com.jsy.domain.SetMenu;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "shop-service-goods",fallback = SetMenuClientImpl.class,configuration = FeignConfiguration.class)
public interface SetMenuClient {
    @GetMapping("/setMenu/getShopIdMenus")
    CommonResult<SetMenu> getShopIdMenus(@RequestParam("shopId") Long shopId);
}
