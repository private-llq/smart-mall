package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.Goods;
import com.jsy.domain.Tree;
import com.jsy.dto.BackstageGoodsDto;
import com.jsy.dto.BackstageServiceDto;
import com.jsy.dto.GoodsDto;
import com.jsy.dto.GoodsServiceDto;
import com.jsy.parameter.GoodsParam;
import com.jsy.parameter.GoodsServiceParam;
import com.jsy.query.BackstageGoodsQuery;
import com.jsy.query.BackstageServiceQuery;
import com.jsy.query.GoodsPageQuery;
import com.jsy.query.NearTheServiceQuery;
import com.jsy.service.IGoodsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private IGoodsService goodsService;




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
        goodsService.delete(id);
        return CommonResult.ok();
    }
    /**
     * 查看一条商品的所有详细信息 B端+C端
     * @param id
     *
     */
    @ApiOperation("查看一条商品的详细信息")
    @GetMapping("/getGoods")
    public CommonResult<GoodsDto> getGoods(@RequestParam("id") Long id)
    {
        GoodsDto goodsDto= goodsService.getGoods(id);
        return CommonResult.ok(goodsDto);
    }
    /**
     * 查看一条服务的所有详细信息 B端+C端
     * @param id
     *
     */
    @ApiOperation("查看一条服务的详细信息")
    @GetMapping("/getGoodsService")
    public CommonResult<GoodsServiceDto> getGoodsService(@RequestParam("id") Long id)
    {
        GoodsServiceDto goodsServiceDto= goodsService.getGoodsService(id);
        return CommonResult.ok(goodsServiceDto);
    }


    /**
     * 查看一条商品或者服务的所有详细信息 B端+C端
     * @param id
     *
     */
    @ApiOperation("查看一条商品或者服务的详细信息")
    @GetMapping("/getByGoods")
    public CommonResult<Goods> getByGoods(@RequestParam("id") Long id)
    {
        Goods goods = goodsService.getByGoods(id);
        return CommonResult.ok(goods);
    }



    /**
     * 查询店铺下面的商品 B端+C端
     * @param goodsPageQuery
     * @return
     */
    @ApiOperation("查询店铺下面的商品")
    @PostMapping("getGoodsAll")
    public CommonResult<PageInfo<GoodsDto>> getGoodsAll(@RequestBody GoodsPageQuery goodsPageQuery)
    {
        PageInfo<GoodsDto> pageInfo = goodsService.getGoodsAll(goodsPageQuery);
        return CommonResult.ok(pageInfo);
    }

    /**
     * 查询店铺下面的服务 B端+C端
     * @param goodsPageQuery
     * @return
     */
    @ApiOperation("查询店铺下面的服务")
    @PostMapping("getGoodsServiceAll")
    public CommonResult<PageInfo<GoodsServiceDto>> getGoodsServiceAll(@RequestBody GoodsPageQuery goodsPageQuery)
    {
        PageInfo<GoodsServiceDto> pageInfo = goodsService.getGoodsServiceAll(goodsPageQuery);
        return CommonResult.ok(pageInfo);
    }

    /**
     *
     * @param backstageGoodsQuery
     * @return
     */
    @ApiOperation("大后台查询商品列表")
    @PostMapping("backstageGetGoodsAll")
    public CommonResult<PageInfo<BackstageGoodsDto>> backstageGetGoodsAll(@RequestBody BackstageGoodsQuery backstageGoodsQuery)
    {
        PageInfo<BackstageGoodsDto> pageInfo = goodsService.backstageGetGoodsAll(backstageGoodsQuery);
        return CommonResult.ok(pageInfo);
    }

    /**
     *
     * @param backstageServiceQuery
     * @return
     */
    @ApiOperation("大后台查询服务列表")
    @PostMapping("backstageGetServiceAll")
    public CommonResult<PageInfo<BackstageServiceDto>> backstageGetServiceAll(@RequestBody BackstageServiceQuery backstageServiceQuery)
    {
        PageInfo<BackstageServiceDto> pageInfo = goodsService.backstageGetServiceAll(backstageServiceQuery);
        return CommonResult.ok(pageInfo);
    }

    /**
     * 大后台屏蔽商家的商品+服务
     * @param id
     */
    @GetMapping("shieldGoods")
    public CommonResult shieldGoods(@RequestParam("id") Long id) {
        goodsService.shieldGoods(id);
       return CommonResult.ok();
    }

    /**
     * 大后台显示商家的商品+服务
     * @param id
     */
    @GetMapping("showGoods")
    public CommonResult showGoods(@RequestParam("id") Long id) {
        goodsService.showGoods(id);
       return CommonResult.ok();
    }

    /**
     * 大后台设置商品+服务的虚拟销量
     * @param id
     */
    @GetMapping("setVirtualSales")
    public CommonResult setVirtualSales(@RequestParam("id") Long id,@RequestParam ("num") Long num) {
        goodsService.virtualSales(id,num);
        return CommonResult.ok();
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


    /**
     * 医疗端：附近的服务
     */
    @PostMapping("/NearTheService")
    public CommonResult<PageInfo<GoodsServiceDto>> NearTheService(@RequestBody NearTheServiceQuery nearTheServiceQuery){
        PageInfo<GoodsServiceDto> pageInfo= goodsService.NearTheService(nearTheServiceQuery);
        return CommonResult.ok(pageInfo);
    }

    /**
     * 医疗端：附近的服务2
     */
    @PostMapping("/NearTheService2")
    public CommonResult<List<GoodsServiceDto>> NearTheService2(@RequestBody NearTheServiceQuery nearTheServiceQuery){
        List<GoodsServiceDto> list= goodsService.NearTheService2(nearTheServiceQuery);
        return CommonResult.ok(list);
    }

    /**
     * 商家被禁用，同步禁用商家的商品和服务
     * type 0 禁用  1 取消
     */
    @GetMapping("/disableAll")
    public CommonResult disableAll(@RequestParam("shopId") Long shopId,@RequestParam("type") Integer type){
         goodsService.disableAll(shopId,type);
        return CommonResult.ok();
    }

    /**
     * 查询状态 ture 正常 false 不正常
     * type ：0 商品  1:服务  2：套餐  3：商店
     */
    @GetMapping("/selectState")
    public CommonResult<Boolean> selectState(@RequestParam("id") Long id,@RequestParam("type") Integer type){
        Boolean state= goodsService.selectState(id,type);
        return CommonResult.ok(state);
    }


    /**
     * 统计店家商品的发布数量 (type=0:商品 1：服务)
     */
    @GetMapping("/getGoodsNumber")
    public CommonResult<Integer> getGoodsNumber(@RequestParam("shopId") Long shopId,@RequestParam("type") Integer type){
        Integer num= goodsService.getGoodsNumber(shopId,type);
        return CommonResult.ok(num);
    }



}

