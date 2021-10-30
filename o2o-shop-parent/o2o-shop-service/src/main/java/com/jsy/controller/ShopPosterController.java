package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.service.IShopPosterService;
import com.jsy.domain.ShopPoster;
import com.jsy.vo.SortVo;
import com.jsy.vo.SortVo2;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/shopPoster")
public class ShopPosterController {
    @Autowired
    public IShopPosterService shopPosterService;
    /**
    * 保存和修改公用的
    * @param shopPoster  传递的实体
    * @return Ajaxresult转换结果
    */
    @ApiOperation("添加或修改店内海报")
    @RequestMapping(value="/pub/save",method= RequestMethod.POST)
    public CommonResult save(@RequestBody ShopPoster shopPoster){

        if (StringUtils.isEmpty(shopPoster.getShopUuid())){
            throw new JSYException(-1,"商店ID参数不能为空");
        }

        try {
            if(StringUtils.isNotEmpty(shopPoster.getUuid())){
                shopPosterService.update(shopPoster,new UpdateWrapper<ShopPoster>().eq("uuid",shopPoster.getUuid()).eq("shop_uuid",shopPoster.getShopUuid()));
            }else{
                shopPoster.setUuid(UUIDUtils.getUUID());
                shopPoster.setState(1);
                shopPosterService.savePoster(shopPoster);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1,"操作失败！");
        }
    }

    /**
    * 删除对象信息
    * @param
    * @return
    */

    @RequestMapping(value="/pub/deletePoster",method=RequestMethod.DELETE)
    public CommonResult deletePoster(@RequestParam String posterUuid,@RequestParam String shopUuid){

            shopPosterService.deletePoster(posterUuid,shopUuid);

       return CommonResult.ok();
    }



    @ApiOperation("获取店内海报信息 根据shopUuid")
    @RequestMapping(value = "/pub/{shopUuid}",method = RequestMethod.GET)
    public CommonResult getPoster(@PathVariable("shopUuid")String shopUuid)
    {
        ShopPoster poster = shopPosterService.getPoster(shopUuid);
        return CommonResult.ok(poster);
    }


    @ApiOperation("商家设置海报关联商品排序")
    @RequestMapping(value = "/pub/setSortGoods",method = RequestMethod.POST)
    public  CommonResult setSortGoods(@RequestBody SortVo2 sortVo2){
        String shopUuid = sortVo2.getShopUuid();
        List<SortVo> sortVo = sortVo2.getSortVo();
        shopPosterService.setSort(shopUuid,sortVo);
        return CommonResult.ok();
    }

    @ApiOperation("商家设置海报排序")
    @RequestMapping(value = "/pub/setSortPoster",method = RequestMethod.POST)
    public  CommonResult setSortPoster(@RequestBody SortVo2 sortVo2){
        String shopUuid = sortVo2.getShopUuid();
        List<SortVo> sortVo = sortVo2.getSortVo();
        shopPosterService.setSort2(shopUuid,sortVo);
        return CommonResult.ok();
    }



    @ApiOperation("商家撤销海报-->state=0")
    @RequestMapping(value = "/pub/outPoster",method = RequestMethod.POST)
    public  CommonResult outPoster(@RequestParam String shopUuid,@RequestParam String posterUuid){
        shopPosterService.update(new UpdateWrapper<ShopPoster>().eq("shop_uuid",shopUuid).eq("uuid",posterUuid).set("state",0));
        return CommonResult.ok();
    }


    /**
     * 根据状态查询 海报
     * @param type 传入状态  1 展示中 2 即将发布 -1已过期  0 停用（撤销中）
     * @return
     */
    @ApiOperation("根据状态查询海报"+"-1已过期  0撤销  1展示中  2即将发布")
    @RequestMapping(value = "/pub/list",method = RequestMethod.GET)
    public CommonResult<List<ShopPoster>> listPoster(@RequestParam String shopUuid,@RequestParam Integer type){
        List<ShopPoster> shopPosters = shopPosterService.listPoster(type,shopUuid);
        return CommonResult.ok(shopPosters);
    }


}
