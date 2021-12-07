package com.jsy.controller;
import com.jsy.basic.util.PageInfo;
import com.jsy.domain.PushGoods;
import com.jsy.query.PushGoodsQuery;
import com.jsy.service.IPushGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

@RestController
@RequestMapping("/pushGoods")
public class PushGoodsController {
    @Autowired
    public IPushGoodsService pushGoodsService;

    /**
     * 医疗端：推送产品（智能手环、手表）
     */
    @GetMapping("/addPushGoods")
    public CommonResult addPushGoods(@RequestParam("id") Long id){
        pushGoodsService.pushGoods(id);
        return CommonResult.ok();
    }

    /**
     * 医疗端：查询推送的产品（智能手环、手表）
     */
    @PostMapping("/pageListPushGoods")
    public CommonResult<PageInfo<PushGoods>> pageListPushGoods(@RequestBody PushGoodsQuery pushGoodsQuery){
        PageInfo<PushGoods> pageInfo=pushGoodsService.pageListPushGoods(pushGoodsQuery);
        return CommonResult.ok(pageInfo);
    }

}
