package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.ServiceCharacteristicsClientImpl;
import com.jsy.domain.ServiceCharacteristics;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "SHOP-SERVICE-BACKSTAGE",fallback = ServiceCharacteristicsClientImpl.class,configuration = FeignConfiguration.class)
public interface ServiceCharacteristicsClient {
    @RequestMapping(value = "/serviceCharacteristics/{id}",method = RequestMethod.GET)
     CommonResult<ServiceCharacteristics> get(@PathVariable("id")Long id);
}
