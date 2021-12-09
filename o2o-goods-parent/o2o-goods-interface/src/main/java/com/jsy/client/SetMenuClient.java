package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.CartClientImpl;
import com.jsy.client.impl.SetMenuClientImpl;
import com.jsy.domain.SetMenu;
import com.jsy.dto.SetMenuDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "shop-service-goods",fallback = SetMenuClientImpl.class,configuration = FeignConfiguration.class)
public interface SetMenuClient {
    @GetMapping("/setMenu/getShopIdMenus")
    CommonResult<SetMenu> getShopIdMenus(@RequestParam("shopId") Long shopId);


    @PostMapping("/setMenu/batchIds")
    CommonResult<List<SetMenuDto>> batchIds(@RequestBody List<Long> ids);

    /**
     * 据id查询套餐和套餐详情
     * @return
     */
    @GetMapping(value = "/setMenu/SetMenuList")
    CommonResult<SetMenuDto> SetMenuList(@RequestParam("id")Long id);
}
