package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "SERVICE-SHOP",configuration = FeignConfiguration.class)
public interface ShopOwnerClient {

    @ApiOperation("根据relationUuid获取店铺拥有者信息")
    @GetMapping("/shopOwner/getByRelationUid/{uuid}")
    public CommonResult getByRelationUid(@PathVariable("uuid")String uuid);


    @ApiOperation("根据登入店铺用户获取店铺拥有者信息")
    @GetMapping("/shopOwner/getByUuid")
    public CommonResult getByUuid();
}
