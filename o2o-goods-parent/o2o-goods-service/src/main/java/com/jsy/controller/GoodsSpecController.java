package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.GoodsSpec;
import com.jsy.mapper.GoodsSpecMapper;
import com.jsy.service.IGoodsBasicService;
import com.jsy.service.IGoodsSpecService;
import com.jsy.service.impl.GoodsSpecServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/goodsSpec")
@Api(tags = "商品规格模块")
public class GoodsSpecController {
    @Resource
    public IGoodsSpecService goodsSpecService;

    @Resource
    public IGoodsBasicService iGoodsBasicService;

    /**
     * 删除对象信息
     *
     * @param uuid
     * @return
     */
    @ApiOperation("根据规格uuid删除规格")
    @RequestMapping(value = "/{uuid}", method = RequestMethod.DELETE)
    public CommonResult<Boolean> delete(@PathVariable("uuid") String uuid) {
     //根规格的uuid查询出商品是否下架，上架中不能删除
        Integer statu = goodsSpecService.selectGoodStatu(uuid);
               if(statu>0){
                   return CommonResult.error(-1, "请先下架商品");
               }
        try {
            goodsSpecService.remove(new QueryWrapper<GoodsSpec>().eq("uuid", uuid));
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1, "删除失败");
        }
    }

    @ApiOperation("根据商品uuid查询状态正常的商品规格")
    @RequestMapping(value = "{uuid}", method = RequestMethod.GET)
    public CommonResult<List<GoodsSpec>> get(@PathVariable("uuid") String uuid) {
        return CommonResult.ok(goodsSpecService.getByGuuid(uuid));
    }

}
