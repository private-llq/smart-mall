package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.ServiceCharacteristicsClientImpl;
import com.jsy.domain.ServiceCharacteristics;
import com.jsy.dto.ServiceCharacteristicsDto;
import com.zhsj.baseweb.annotation.LoginIgnore;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "shop-service-backstage",fallback = ServiceCharacteristicsClientImpl.class,configuration = FeignConfiguration.class)
public interface ServiceCharacteristicsClient {
//    @RequestMapping(value = "/serviceCharacteristics/{id}",method = RequestMethod.GET)
//     CommonResult<ServiceCharacteristics> get(@PathVariable("id")Long id);
//
//    @ApiOperation("返回list列表")
//    @GetMapping(value = "/serviceCharacteristics/getList")
//    public CommonResult<List<ServiceCharacteristicsDto>> getList(@RequestParam("serviceId") String serviceId);
}
