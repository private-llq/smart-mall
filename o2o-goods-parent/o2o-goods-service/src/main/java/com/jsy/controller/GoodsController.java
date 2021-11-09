package com.jsy.controller;
import cn.hutool.core.bean.BeanUtil;
import com.jsy.dto.GoodsDto;
import com.jsy.dto.GoodsServiceDto;
import com.jsy.parameter.GoodsParam;
import com.jsy.parameter.GoodsServiceParam;
import com.jsy.service.IGoodsService;
import com.jsy.domain.Goods;
import com.jsy.query.GoodsQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    public IGoodsService goodsService;

    /**
    * 添加 商品
    */
    @ApiOperation("添加商品")
    @PostMapping(value="/saveGoods")
    public CommonResult saveGoods(@RequestBody GoodsParam goodsParam){
        goodsService.saveGoods(goodsParam);
        return CommonResult.ok();
    }

    /**
     * 添加 服务
     */
    @ApiOperation("添加服务")
    @PostMapping(value="/saveService")
    public CommonResult saveService(@RequestBody GoodsServiceParam goodsServiceParam){
        goodsService.saveService(goodsServiceParam);
        return CommonResult.ok();

    }

    /**
    * 删除商品或者服务
    * @param id
    * @return
    */
    @ApiOperation("删除商品或者服务")
    @DeleteMapping("delete")
    public CommonResult delete(@RequestParam("id") Long id){
        try {
            goodsService.removeById(id);
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return  CommonResult.error(-1,"删除失败！");
        }
    }

    /**
     * 查看一条商品的所有信息
     * @param id
     *
     */
    @ApiOperation("查看一条商品/服务的所有信息")
    @GetMapping("getGoodsService")
    public CommonResult<Goods> getGoodsService(@RequestParam("id") Long id)
    {
        Goods goods= goodsService.getGoodsService(id);
        return CommonResult.ok(goods);
    }

    /**
     * 查询店铺下面的所有商品+服务
     */
    @ApiOperation("查询店铺下面的所有商品+服务")
    @GetMapping("getGoodsAll")
    public CommonResult<Map<Integer,List<Goods>>> getGoodsAll(@RequestParam("shopId") Long shopId)
    {
        Map<Integer,List<Goods>> map = goodsService.getGoodsAll(shopId);
        return CommonResult.ok(map);
    }

}