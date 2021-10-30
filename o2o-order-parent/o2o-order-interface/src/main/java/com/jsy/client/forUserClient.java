package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.OrderClientImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "SERVICE-ORDER",fallback = OrderClientImpl.class,configuration = FeignConfiguration.class)
public interface forUserClient {

    @RequestMapping(value = "/user22",method = RequestMethod.GET)
    public CommonResult dealTokenSheQu();
}
