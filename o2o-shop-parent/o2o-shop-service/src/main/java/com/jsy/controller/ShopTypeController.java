package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.ShopType;
import com.jsy.service.IShopTypeService;
import com.jsy.vo.ShopTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/shopType")
@Api(tags = "店铺类别模块")
public class ShopTypeController {
    @Autowired
    public IShopTypeService shopTypeService;





    @RequestMapping(value="/selectShopType",method= RequestMethod.GET)
    @ApiOperation("根据门店类型uuid查询所属类型层级系")
    public CommonResult <String>selectShopType(String uuid){
        String s = shopTypeService.selectShopType(uuid);
        return  new CommonResult<>(200,"查询成功",s);
    }




    /**
    * 保存和修改公用的
    * @param shopType  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    @ApiOperation("保存，修改店铺信息")
    public CommonResult<Boolean> save(@RequestBody ShopType shopType){
        try {
            if(shopType.getUuid()!=null){
                shopTypeService.update(shopType,new UpdateWrapper<ShopType>().eq("uuid",shopType.getUuid()));
            }else{
                shopType.setUuid(UUIDUtils.getUUID());
                shopTypeService.save(shopType);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1,"操作失败");
        }
    }
    /**
    * 删除店铺信息
    * @param uuid
    * @return
    */
    @RequestMapping(value="/{uuid}",method=RequestMethod.DELETE)
    @ApiOperation("删除店铺类别")
    public CommonResult<Boolean> delete(@PathVariable("uuid") String uuid){
        try {
            //todo 联级删除
            shopTypeService.remove(new QueryWrapper<ShopType>().eq("uuid",uuid));
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1,"删除失败");
        }
    }

    /**
     * 获取所有分类tree 结构
     * @return
     */
    @RequestMapping(value = "/pub/allList",method = RequestMethod.GET)
    @ApiOperation("获取所有店铺分类")
    public CommonResult<List<ShopTypeVo>> getList() {
        return CommonResult.ok(shopTypeService.getList());
    }
    /**
    * 根据父类id查询店铺类型
    * @return
    */
    @ApiOperation("根据父类id查询店铺类型")
    @RequestMapping(value = "/pub/list/{parentId}",method = RequestMethod.GET)
    public CommonResult<List<ShopType>> findByParentId(@PathVariable("parentId") Long parentId){
        return CommonResult.ok(shopTypeService.findByParentId(parentId));
    }

}
