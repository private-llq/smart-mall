package com.jsy.client;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.UserAddressClientImpl;
import com.jsy.domain.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "SERVICE-USER",fallback = UserAddressClientImpl.class)
public interface UserAddressClient {

    @RequestMapping(value = "/userAddress/getByUuid/{uuid}",method = RequestMethod.GET)
    CommonResult<UserAddress> getByUuid(@PathVariable("uuid")String uuid);

}
