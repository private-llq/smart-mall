package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.TreeClientImpl;
import com.jsy.domain.Tree;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "shop-service-backstage",fallback = TreeClientImpl.class,configuration = FeignConfiguration.class)
public interface TreeClient {
    @GetMapping("/tree/selectAllTree/")
    CommonResult<List<Tree>> selectAllTree(@RequestParam("id") Long id);

    @RequestMapping(value = "/tree/getTree",method = RequestMethod.GET)
     CommonResult<Tree> getTree(@RequestParam("id")Long id);

    /**
     * 查询本级上面所有父级菜单（不包含本级）
     */
    @GetMapping("/tree/getParentTreeAll")
    CommonResult<String> getParentTreeAll(@RequestParam ("id") Long id);
}
