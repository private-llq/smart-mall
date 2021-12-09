package com.jsy.controller;
import com.jsy.basic.util.PageInfo;
import com.jsy.client.HotClient;
import com.jsy.dto.NewShopHotDto;
import com.jsy.query.NewShopQuery;
import com.jsy.service.IHotGoodsService;
import com.jsy.domain.HotGoods;
import com.jsy.query.HotGoodsQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/hotGoods")
public class HotGoodsController {
    @Autowired
    public IHotGoodsService hotGoodsService;
    @Autowired
    public HotClient hotClient;
    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @DeleteMapping(value="/{id}")
    public CommonResult delete(@PathVariable("id") Long id){
        try {
            hotGoodsService.removeById(id);

            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return  CommonResult.error(-1,"删除失败！");
        }
    }
    @ApiOperation("热门推荐")
    @RequestMapping(value = "/getHot",method = RequestMethod.POST)
    public CommonResult<PageInfo<HotGoods>> getHot(@RequestBody NewShopQuery newShopQuery){
        PageInfo<HotGoods> hotDtoPageInfo = hotGoodsService.getHot(newShopQuery);
        if (hotDtoPageInfo!=null){
            return CommonResult.ok(hotDtoPageInfo);
        }
        else {
            return new  CommonResult(-1,"失败",null);
        }
    }
    @ApiOperation("根据商品id彻底删除热门数据表")
    @RequestMapping(value = "/delHotGoods",method = RequestMethod.POST)
    public CommonResult<Boolean> delHotGoods(@RequestParam("goodsId") Long goodsId){
        Boolean b = hotGoodsService.delHotGoods(goodsId);
        if (b){
            return CommonResult.ok(true);
        }
        else {
            return new  CommonResult(-1,"失败",false);
        }
    }
    @ApiOperation("根据店铺id彻底删除热门数据表")
    @RequestMapping(value = "/delHotShop",method = RequestMethod.POST)
    public CommonResult<Boolean> delHotShop(@RequestParam("shopId") Long shopId){
        Boolean b = hotGoodsService.delHotShop(shopId);
        if (b){
            return CommonResult.ok(true);
        }
        else {
            return new  CommonResult(-1,"失败",false);
        }
    }
    @ApiOperation("根据商品id查询是否是热门数据")
    @RequestMapping(value = "/getHotGoods",method = RequestMethod.POST)
    public CommonResult<HotGoods> getHotGoods(@RequestParam("goodsId") Long goodsId){
        HotGoods hotGoods = hotGoodsService.getHotGoods(goodsId);
        return CommonResult.ok(hotGoods);
    }
    @ApiOperation("热根据店铺id查询是否是门数据")
    @RequestMapping(value = "/getHotShop",method = RequestMethod.POST)
    public CommonResult<HotGoods> getHotShop(@RequestParam("shopId") Long shopId){
        HotGoods hotGoods = hotGoodsService.getHotShop(shopId);
        return CommonResult.ok(hotGoods);
    }
    @ApiOperation("查询所有热门数据")
    @RequestMapping(value = "/getHotList",method = RequestMethod.POST)
    public CommonResult<List<HotGoods>> getHotList(){
        List<HotGoods> hotList = hotGoodsService.getHotList();
        return CommonResult.ok(hotList);
    }


}
