package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.FileClient;
import com.jsy.domain.ShopSignsTemplate;
import com.jsy.service.IShopPosterTemplateService;
import com.jsy.domain.ShopPosterTemplate;
import com.jsy.query.ShopPosterTemplateQuery;
import com.jsy.basic.util.AjaxResult;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/shopPosterTemplate")
public class ShopPosterTemplateController {
    @Autowired
    public IShopPosterTemplateService shopPosterTemplateService;

    @Autowired
    private FileClient fileClient;
    /**
     * 保存和修改公用的
     * @param shopPosterTemplate  传递的实体
     * @return Ajaxresult转换结果
     */
    @ApiOperation("保存和修改一个海报模板")
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public CommonResult save(@RequestBody ShopPosterTemplate shopPosterTemplate){
        try {
            if(StringUtils.isNotEmpty(shopPosterTemplate.getUuid())){
                shopPosterTemplateService.update(shopPosterTemplate,new UpdateWrapper<ShopPosterTemplate>().eq("uuid",shopPosterTemplate.getUuid()));
            }else{
                shopPosterTemplate.setUuid(UUIDUtils.getUUID());
                shopPosterTemplateService.save(shopPosterTemplate);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1,"操作失败!");
        }
    }

    @ApiOperation("根据id删除一个海报模板")
    @RequestMapping(value="/{uuid}",method=RequestMethod.DELETE)
    public CommonResult delete(@PathVariable("uuid") String uuid){
        try {
            shopPosterTemplateService.remove(new QueryWrapper<ShopPosterTemplate>().eq("uuid",uuid));
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return CommonResult.error(-1,"操作失败!");
        }
    }

    @ApiOperation("根据id查询一个海报模板")
    @RequestMapping(value = "/{uuid}",method = RequestMethod.GET)
    public ShopPosterTemplate get(@PathVariable("uuid")String uuid)
    {
        return shopPosterTemplateService.getOne(new QueryWrapper<ShopPosterTemplate>().eq("uuid",uuid));
    }


    /**
    * 查看所有的信息
    * @return
    */
    @ApiOperation("查询所有海报模板")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResult list(){

        List<ShopPosterTemplate> list = shopPosterTemplateService.list(null);

        return CommonResult.ok(list);

    }


}
