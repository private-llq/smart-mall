package com.jsy.controller;
import com.jsy.basic.util.PageInfo;
import com.jsy.dto.BackstageGoodsDto;
import com.jsy.param.BackstageGoodsParam;
import com.jsy.service.IBackstageGoodsService;
import com.jsy.domain.BackstageGoods;
import com.jsy.query.BackstageGoodsQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/backstageGoods")
public class BackstageGoodsController {
    @Autowired
    public IBackstageGoodsService backstageGoodsService;


    @PostMapping(value="/addBackstageGoods")
    public CommonResult addBackstageGoods(@RequestBody BackstageGoodsParam backstageGoodsParam){
        backstageGoodsService.addBackstageGoods(backstageGoodsParam);
        return CommonResult.ok();
    }

    @PostMapping(value="/updateBackstageGoods")
    public CommonResult updateBackstageGoods(@RequestBody BackstageGoodsParam backstageGoodsParam){
        backstageGoodsService.updateBackstageGoods(backstageGoodsParam);
        return CommonResult.ok();
    }


    @DeleteMapping(value="id")
    public CommonResult deleteBackstageGoods(@RequestParam("id") Long id){
        backstageGoodsService.removeById(id);
       return CommonResult.ok();
    }




    @PostMapping(value = "/pageBackstageGoods")
    public CommonResult<PageInfo<BackstageGoodsDto>> pageBackstageGoods(@RequestBody BackstageGoodsQuery query)
    {
        PageInfo<BackstageGoodsDto> pageInfo=backstageGoodsService.pageBackstageGoods(query);
       return  CommonResult.ok(pageInfo);
    }
}
