package com.jsy.controller;
import com.jsy.basic.util.PageInfo;
import com.jsy.domain.PushGoods;
import com.jsy.parameter.PushGoodsParam;
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
     * type 推送状态：0 未推送  1：医疗 2 ：养老 3 商城
     */
    @PostMapping("/addPushGoods")
    public CommonResult addPushGoods(@RequestBody PushGoodsParam pushGoodsParam){
        pushGoodsService.pushGoods(pushGoodsParam);
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

    /**
     * 取消推送
     */
    @GetMapping("/outPushGoodsSort")
    public CommonResult outPushGoodsSort(@RequestParam("goodsId") Long goodsId){
        pushGoodsService.outPushGoodsSort(goodsId);
        return CommonResult.ok();
    }

}
