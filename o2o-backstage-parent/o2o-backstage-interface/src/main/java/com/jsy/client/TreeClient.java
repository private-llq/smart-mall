package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.ServiceCharacteristicsClientImpl;
import com.jsy.client.impl.TreeClientImpl;
import com.jsy.domain.Tree;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "SHOP-SERVICE-BACKSTAGE",fallback = TreeClientImpl.class,configuration = FeignConfiguration.class)
public interface TreeClient {
    @GetMapping("/tree/selectAllTree")
    CommonResult<Boolean> selectAllTree(@RequestParam("id") Integer id);

    @RequestMapping(value = "/tree/{id}", method = RequestMethod.GET)
    Tree getTree(@PathVariable("id") Long id);
}
