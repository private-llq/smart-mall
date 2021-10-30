package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.constant.Global;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.domain.ShopAssets;
import com.jsy.query.ShopAssetsQuery;
import com.jsy.service.IShopAssetsService;
import com.jsy.vo.ShopAssetsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/shopAssets")
@Api(tags = "资产表controller")
public class ShopAssetsController {

    @Autowired
    public IShopAssetsService shopAssetsService;


    /**
    * 新增资产信息
    * @param shopAssets  传递的实体
    * @return
    */
    @ApiOperation("新增资产信息")
    @RequestMapping(value="/saveAndUpdate",method= RequestMethod.POST)
    public CommonResult save(@RequestBody ShopAssetsVO shopAssets){
        shopAssetsService.add(shopAssets);
        return CommonResult.ok();
    }

    @ApiOperation("修改账户卡号信息")
    @PostMapping("/updateCard")
    public CommonResult updateCard(@RequestBody ShopAssetsVO shopAssetsVO) {
        shopAssetsService.updateCard(shopAssetsVO);
        return CommonResult.ok();
    }

    @ApiOperation("查看账户资产信息")
    @GetMapping("/select")
    public CommonResult<ShopAssets> select() {
        return CommonResult.ok(shopAssetsService.select());
    }

    //获取
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ShopAssets get(@PathVariable("id")Long id) {
        return shopAssetsService.getById(id);
    }


    /**
    * 查看所有的信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<ShopAssets> list(){
        return shopAssetsService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public PageList<ShopAssets> json(@RequestBody ShopAssetsQuery query) {
        Page<ShopAssets> page = new Page<ShopAssets>(query.getPage(),query.getRows());
        page = shopAssetsService.page(page);
        return new PageList<ShopAssets>(page.getTotal(),page.getRecords());
    }
    /////***************************************************************************************************************************////


    @ApiOperation("新增资产信息")
    @PostMapping("/addAsset")
    public CommonResult addAsset(@RequestBody ShopAssetsVO shopAssetsVO){
        int i = shopAssetsService.addAsset(shopAssetsVO);
        if (i== Global.INT_0){
            return CommonResult.error(-1,"操作失败");
        }
        return CommonResult.ok();
    }


    @ApiOperation("修改账户卡号信息")
    @PostMapping("/updateIdCard")
    public CommonResult updateIdCard(@RequestBody ShopAssetsVO shopAssetsVo){
        int i = shopAssetsService.updateIdCard(shopAssetsVo);
        if (i==Global.INT_0){
            return CommonResult.error(-1,"修改失败");
        }
        return CommonResult.ok();
    }

    @ApiOperation("修改账户资金信息")
    @PutMapping("/updateMoney")
    public CommonResult updateAssets(@RequestParam("type")Integer type,
                                     @RequestParam("num")BigDecimal num,
                                     @RequestParam("uuid")String uuid){
            int i = shopAssetsService.updateMoney(type,num,uuid);
            if (i==Global.INT_0){
                return CommonResult.error(-1,"修改资金失败");
            }
            return CommonResult.ok();
    }

    @ApiOperation("查看账户资产信息")
    @GetMapping("/get")
    public CommonResult Get(){
        UserDto user = CurrentUserHolder.getCurrentUser();
        ShopAssets shopAssets = shopAssetsService.getOne(new QueryWrapper<ShopAssets>().eq("owner_uuid", user.getUuid()));
        return CommonResult.ok(shopAssets.getAssets());
    }
}
