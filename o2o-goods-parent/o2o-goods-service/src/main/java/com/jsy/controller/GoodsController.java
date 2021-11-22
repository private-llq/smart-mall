package com.jsy.controller;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.domain.Tree;
import com.jsy.dto.GoodsDto;
import com.jsy.dto.GoodsServiceDto;
import com.jsy.parameter.GoodsParam;
import com.jsy.parameter.GoodsServiceParam;
import com.jsy.query.GoodsPageQuery;
import com.jsy.service.IGoodsService;
import com.jsy.domain.Goods;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    public IGoodsService goodsService;



    /**
    * 添加 商品
    */
    @ApiOperation("添加商品")
    @PostMapping(value="saveGoods")
    public CommonResult saveGoods(@RequestBody GoodsParam goodsParam){
        goodsService.saveGoods(goodsParam);
        return CommonResult.ok();
    }

    /**
     * 添加 服务
     */
    @ApiOperation("添加服务")
    @PostMapping(value="saveService")
    public CommonResult saveService(@RequestBody GoodsServiceParam goodsServiceParam){
        goodsService.saveService(goodsServiceParam);
        return CommonResult.ok();

    }

    /**
     * 添加服务查询分类
     * @param shopId
     * @return
     */
    @ApiOperation("添加服务查询分类")
    @GetMapping(value="selectServiceType")
    public CommonResult<List<Tree>> selectServiceType(@RequestParam("shopId") Long shopId){
        List<Tree> list= goodsService.selectServiceType(shopId);
        return CommonResult.ok(list);

    }

    /**
     * 上架商品/服务
     * @param id
     */

    @ApiOperation("上架商品/服务")
    @GetMapping(value="putaway")
    public CommonResult putaway(@RequestParam ("id") Long id){
        goodsService.putaway(id);
        return CommonResult.ok();

    }

    /**
     * 下架商品/服务
     * @param id
     */
    @ApiOperation("下架商品/服务")
    @GetMapping(value="outaway")
    public CommonResult outaway(@RequestParam ("id") Long id){
        goodsService.outaway(id);
        return CommonResult.ok();

    }

    /**
     * 一键上架商品/服务
     */
    @ApiOperation("一键上架商品/服务")
    @PostMapping(value="putawayAll")
    public CommonResult putawayAll(@RequestBody List<Long> idList){
        goodsService.putawayAll(idList);
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
     * 查看一条商品/服务的详细信息
     * @param id
     *
     */
    @ApiOperation("查看一条商品/服务的详细信息")
    @GetMapping("getGoodsService")
    public CommonResult<Goods> getGoodsService(@RequestParam("id") Long id)
    {
        Goods goods= goodsService.getGoodsService(id);
        return CommonResult.ok(goods);
    }

    /**
     *
     * @param goodsPageQuery
     * @return
     */
    @ApiOperation("查询店铺下面的商品+服务")
    @PostMapping("getGoodsAll")
    public CommonResult<PageInfo<Goods>> getGoodsAll(@RequestBody GoodsPageQuery goodsPageQuery)
    {
        PageInfo<Goods> pageInfo = goodsService.getGoodsAll(goodsPageQuery);
        return CommonResult.ok(pageInfo);
    }

    @ApiOperation("查询商家最新发布的商品或在服务")
    @GetMapping("/getShopIdGoods")
    public CommonResult<Goods> getShopIdGoods(@RequestParam("shopId") Long shopId)
    {
        List<Goods> one = goodsService.list(new QueryWrapper<Goods>().eq("shop_id", shopId).orderByDesc("create_time"));
        if (one.size()>0){
            Goods goods = one.get(0);
            return CommonResult.ok(goods);
        }
        return  CommonResult.ok(null);
    }


    /**
     * 最新上架 商品、服务、套餐
     * @param shopId
     * @return
     */
    @GetMapping("/latelyGoods/{shopId}")
    public CommonResult<Goods> latelyGoods(@PathVariable("shopId") Long shopId)
    {
         Goods goods=  goodsService.latelyGoods(shopId);

        return CommonResult.ok(goods);
    }

    /**
     * 批量查询 商品
     */

    @PostMapping("/batchGoods")
    public CommonResult<List <GoodsDto>> batchGoods(@RequestBody List<Long> goodsList)
    {
       List <GoodsDto> goodsDtoList=  goodsService.batchGoods(goodsList);
        return CommonResult.ok(goodsDtoList);
    }

    /**
     * 批量查询 服务
     */
    @PostMapping("/batchGoodsService")
    public CommonResult<List<GoodsServiceDto>>  batchGoodsService(@RequestBody List<Long> goodsServiceList) {
        List<GoodsServiceDto> goodsServiceDtos = goodsService.batchGoodsService(goodsServiceList);
        return CommonResult.ok(goodsServiceDtos);
    }
}

