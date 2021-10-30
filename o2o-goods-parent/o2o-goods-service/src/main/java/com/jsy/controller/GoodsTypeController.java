package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.GoodsBasic;
import com.jsy.domain.GoodsType;
import com.jsy.dto.GoodsTypeDTO;
import com.jsy.parameter.GoodsSortParam;
import com.jsy.service.IGoodsBasicService;
import com.jsy.service.IGoodsTypeService;
import com.jsy.vo.GoodsTypeUpdateVo;
import com.jsy.vo.GoodsTypeVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "商品分类")
@RequestMapping("/goodsType")
public class GoodsTypeController {
    @Autowired
    public IGoodsTypeService goodsTypeService;

    @Autowired
    private IGoodsBasicService goodsBasicService;

    /**
    * 新增商品分类
    *
    */
    @ApiOperation("新增商品分类")
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public CommonResult<Boolean> save(@RequestBody GoodsTypeVo goodsTypeVo){
        GoodsType goodsType=new GoodsType();
        BeanUtils.copyProperties(goodsTypeVo,goodsType);
                goodsType.setUuid(UUIDUtils.getUUID());
        goodsType.setRanks(0);
        goodsType.setShopUuid(CurrentUserHolder.getCurrentUser().getUuid());
        boolean save = goodsTypeService.save(goodsType);
        if(!save){
            return CommonResult.error(2004,"新增失败");
        }
        return CommonResult.ok();

    }

    /**
     * 查询店铺中所有商品分类
     *
     */
    @ApiOperation("查询店铺中所有商品分类")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResult<List<GoodsType>> listTree(){
        QueryWrapper<GoodsType> qw = new QueryWrapper<GoodsType>();
        qw.eq("shop_uuid", CurrentUserHolder.getCurrentUser().getUuid());
        //qw.orderByAsc("ranks");
        qw.orderByDesc("ranks");
        List<GoodsType> list = goodsTypeService.list(qw);
        return new CommonResult<List<GoodsType>>(200,"查询成功",list) ;
    }

    /**
     * 修改商品分类的顺序
     *
     */
    @ApiOperation("修改商品分类的顺序")
    @RequestMapping(value = "/sort",method = RequestMethod.POST)
    public CommonResult<Boolean> sort(@RequestBody List<GoodsSortParam> SortS){
        GoodsType goodsType=new GoodsType();
        SortS.forEach((s)->{
            BeanUtils.copyProperties(s,goodsType);
            boolean uuid = goodsTypeService.update(goodsType, new UpdateWrapper<GoodsType>().eq("uuid", s.getUuid()));
        });
        return new CommonResult<Boolean>(200,"修改成功",true) ;
    }

    /**
     * 修改商品分类
     *
     */
    @ApiOperation("修改商品分类")//修改商品分类，应该没有使用该分类的时候修改，一般不会修改商品的分类
    @RequestMapping(value="/update",method= RequestMethod.PUT)
    public CommonResult<Boolean> update(@RequestBody GoodsTypeUpdateVo goodsTypeUpdateVo){
        if (goodsTypeUpdateVo.getUuid()==null) {
            throw  new JSYException(2005,"没有数据");
        }
        List<GoodsBasic> goodsByType1 = goodsTypeService.findGoodsByTypeId(goodsTypeUpdateVo.getUuid());
        if(goodsByType1.size()>0){//该类型有商品在使用
            throw  new JSYException(2005,"类型在使用中，不能修改商品分类");
        }
        String shopUuid = CurrentUserHolder.getCurrentUser().getUuid();
        GoodsType goodsType = goodsTypeService.getOne(new QueryWrapper<GoodsType>().eq("uuid", goodsTypeUpdateVo.getUuid()).eq("shop_uuid", shopUuid));
        if(goodsType==null){
            throw new JSYException(2004,"修改商品分类不存在或不属于该商家");
        }
         BeanUtils.copyProperties(goodsTypeUpdateVo,goodsType);
        boolean uuid = goodsTypeService.update(goodsType, new UpdateWrapper<GoodsType>().eq("uuid", goodsType.getUuid()));
        if(uuid){
            return CommonResult.ok();
        }
        return CommonResult.error(2004,"修改失败");
    }

    /**
    * 根据分类id删除分类
    * @param uuid
    * @return
    */
    @ApiOperation("根据分类uuid删除分类")//删除商品分类，应该没有使用该分类的时候删除，一般不会删除商品的分类
    @RequestMapping(value="/{uuid}",method=RequestMethod.DELETE)
    public CommonResult<Boolean> delete(@PathVariable("uuid") String uuid){
        List<GoodsBasic> goodsByType1 = goodsTypeService.findGoodsByTypeId(uuid);
        if(goodsByType1.size()>0){//该类型有商品在使用
            throw  new JSYException(2005,"类型在使用中，不能删除商品分类");
        }
        CommonResult<List<GoodsBasic>> listCommonResult = get(uuid);
        List<GoodsBasic> data = listCommonResult.getData();
        if(data.size()>0){//该类型没有商品在使用
            throw  new JSYException(2005,"类型在使用中，不能删除商品分类");
        }
        boolean b = goodsTypeService.deleteByUuid(uuid);
        return   new CommonResult<Boolean>(200,"操作成功",b);

    }






    /**************************************************************************/
    /**
     * 根据商品分类获取商品
     * @param uuid
     * @return
     */
    @ApiOperation("根据商品分类uuid获取商品")
    @RequestMapping(value = "/{uuid}",method = RequestMethod.GET)
    public CommonResult<List<GoodsBasic>> get(@PathVariable("uuid")String uuid) {

        return   new CommonResult<List<GoodsBasic>>(200,"操作成功",goodsTypeService.findGoodsByTypeId(uuid));
    }
    @ApiOperation("根据商铺uuid查询商品每个类型下的各个商品")
    @GetMapping("/shop/pub/{uuid}")
    public CommonResult<List<GoodsTypeDTO>> findGoodsType(@ApiParam("店铺uuid") @PathVariable("uuid") String uuid) {
        return CommonResult.ok(goodsTypeService.findGoodsType(uuid));
    }

}
