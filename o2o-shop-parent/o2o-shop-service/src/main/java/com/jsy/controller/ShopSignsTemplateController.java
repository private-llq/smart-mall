package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.FileClient;
import com.jsy.query.NewUserQuery;
import com.jsy.service.IShopSignsTemplateService;
import com.jsy.domain.ShopSignsTemplate;
import com.jsy.query.ShopSignsTemplateQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/shopSignsTemplate")
public class ShopSignsTemplateController {

    @Autowired
    private IShopSignsTemplateService shopSignsTemplateService;

    @Autowired
    private FileClient fileClient;


    @ApiOperation("修改或删除招牌模板")
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public CommonResult save(@RequestBody ShopSignsTemplate shopSignsTemplate){
        try {
            if(StringUtils.isNotEmpty(shopSignsTemplate.getUuid())){
                shopSignsTemplateService.update(shopSignsTemplate,new UpdateWrapper<ShopSignsTemplate>().eq("uuid",shopSignsTemplate.getUuid()));
            }else{
                shopSignsTemplateService.save(shopSignsTemplate);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1,"操作失败！");
        }
    }

    @ApiOperation("根据uuid删除一个模板")
    @RequestMapping(value="/{uuid}",method=RequestMethod.DELETE)
    public CommonResult delete(@PathVariable("uuid") String uuid){
        try {
            shopSignsTemplateService.remove(new QueryWrapper<ShopSignsTemplate>().eq("uuid",uuid));
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return CommonResult.error(-1,"操作失败！");
        }
    }

    @ApiOperation("根据uuid查询一个招牌模板")
    @RequestMapping(value = "/{uuid}",method = RequestMethod.GET)
    public ShopSignsTemplate get(@PathVariable("uuid")String uuid)
    {
        return shopSignsTemplateService.getOne(new QueryWrapper<ShopSignsTemplate>().eq("uuid",uuid));
    }

    /**
    * 查看所有的模板信息
    * @return
    */
    @ApiOperation("查询所有招牌模板")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResult list(){

        List<ShopSignsTemplate> list = shopSignsTemplateService.list(null);

        return CommonResult.ok(list);
    }


}
