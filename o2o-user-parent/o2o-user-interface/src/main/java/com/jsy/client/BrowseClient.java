package com.jsy.client;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.UserAddressClientImpl;
import com.jsy.domain.Browse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "shop-service-user",fallback = UserAddressClientImpl.class)
public interface BrowseClient  {
    @PostMapping(value="/browse/save/")
    public CommonResult save(@RequestBody Browse browse);

    @PostMapping(value = "/browse/selectOne")
    public CommonResult<Browse> selectOne(@RequestParam("goodsId") Long goodsId, @RequestParam("userId") Long userId);
}
