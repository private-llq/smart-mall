package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.SetProperties;
import com.jsy.query.SetPropertiesQuery;
import com.jsy.service.ISetPropertiesService;
import com.jsy.vo.SetPropertiesVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/setProperties")
@Api("设置属性服务")
public class SetPropertiesController {

    @Autowired
    public ISetPropertiesService setPropertiesService;

    @ApiOperation(value = "设置新增",httpMethod = "POST",response = CommonResult.class)
    @Transactional
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResult save(SetPropertiesVo setPropertiesVo){
        int i = setPropertiesService.save(setPropertiesVo);
        if (i==1){
            return CommonResult.ok();
        }
        return CommonResult.error(-1,"新增失败,请不要新增已有的模块");
    }


    @ApiOperation(value = "设置修改",httpMethod = "POST",response = CommonResult.class)
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public CommonResult update(SetPropertiesVo setPropertiesVo){
        int i = setPropertiesService.updateById(setPropertiesVo);

        if (i==1){
            return CommonResult.ok();
        }
        return CommonResult.error(-1,"修改失败");
    }

    @ApiOperation(value = "获取一个",httpMethod = "GET",response = CommonResult.class)
    @RequestMapping(value = "/selectOne/{uuid}",method = RequestMethod.GET)
    public CommonResult selectOne(@PathVariable String uuid){
        SetProperties setProperties = setPropertiesService.getById(uuid);
        if (Objects.isNull(setProperties)){
            return CommonResult.error(-1,"获取失败请稍后");
        }

        return CommonResult.ok(setProperties);
    }

    @ApiOperation(value = "获取分页数据",httpMethod = "POST",response = CommonResult.class)
    @RequestMapping(value = "/pageList",method = RequestMethod.POST)
    public CommonResult pageList(SetPropertiesQuery query){
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(StringUtils.isNotEmpty(query.getModule()),"for_module",query.getModule());
        //生成分页规则
        Page<SetProperties> propertiesPage = new Page<SetProperties>(query.getPage(), query.getRows());
        Page page = setPropertiesService.page(propertiesPage, queryWrapper);
        return CommonResult.ok(new PageList<SetProperties>(page.getTotal(),page.getRecords()));
    }

}
