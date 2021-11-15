package com.jsy.controller;
import com.jsy.dto.GoodsTypeDto;
import com.jsy.param.GoodsTypeParam;
import com.jsy.service.IGoodsTypeService;
import com.jsy.domain.GoodsType;
import com.jsy.query.IndustryCategoryQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/industryCategory")
public class GoodsTypeController {
    @Autowired
    public IGoodsTypeService goodsTypeService;

    /**
     * 保存和修改公用的
     * @param typeParam  传递的实体
     * @return Ajaxresult转换结果
     */
    @ApiOperation(value = "新增或删除")
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody GoodsTypeParam typeParam){
        try {
            if(typeParam.getId()!=null){
                GoodsType goodsType = new GoodsType();
                BeanUtils.copyProperties(typeParam,goodsType);
                goodsTypeService.updateById(goodsType);
            }else{
                GoodsType goodsType = new GoodsType();
                BeanUtils.copyProperties(typeParam,goodsType);
                goodsTypeService.save(goodsType);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1,"操作失败！");
        }
    }

    /**
     * 删除对象信息
     * @param id
     * @return
     */
    @ApiOperation(value = "删除对象信息",httpMethod = "Delete",response = CommonResult.class)
    @DeleteMapping(value="/{id}")
    public CommonResult delete(@PathVariable("id") Long id){
        try {
            goodsTypeService.deleteById(id);
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return  CommonResult.error(-1,"删除失败！");
        }
    }

    /**
     * 根据id查询一条
     * @param id
     */
    @ApiOperation("根据id查询")
    @GetMapping(value = "/{id}")
    public CommonResult<GoodsTypeDto> get(@PathVariable("id")Long id)
    {
        GoodsType goodsType = null;
        try {
            goodsType = goodsTypeService.getById(id);
            GoodsTypeDto goodsTypeDto = new GoodsTypeDto();
            BeanUtils.copyProperties(goodsType,goodsTypeDto);
            return   CommonResult.ok(goodsTypeDto);
        } catch (Exception e) {
            e.printStackTrace();
            return  new CommonResult<>(-1,"查询失败！",null);
        }


    }


    /**
     * 返回list列表
     * @return
     */
    @ApiOperation(value = "查询分类结果",httpMethod = "GET",response = CommonResult.class)
    @GetMapping(value = "/list")
    public CommonResult<List<GoodsTypeDto>> list(){
        List<GoodsType> list = goodsTypeService.selectCategory();
        List<GoodsTypeDto> dtoList = new ArrayList<>();
        for (GoodsType goodsType : list) {
            GoodsTypeDto goodsTypeDto = new GoodsTypeDto();
            BeanUtils.copyProperties(goodsType,goodsTypeDto);
            dtoList.add(goodsTypeDto);
        }
        return CommonResult.ok(dtoList);
    }


    /**
     * 分页查询数据
     *
     * @param query 查询对象
     * @return PageList 分页对象
     */
//    @ApiOperation(value = "获取分页数据",httpMethod = "POST",response = CommonResult.class)
    @PostMapping(value = "/pagelist")
    public PageList<GoodsType> json(@RequestBody IndustryCategoryQuery query)
    {
        Page<GoodsType> page = new Page<GoodsType>(query.getPage(),query.getRows());
        page = goodsTypeService.page(page);
        return new PageList<GoodsType>(page.getTotal(),page.getRecords());
    }
}
