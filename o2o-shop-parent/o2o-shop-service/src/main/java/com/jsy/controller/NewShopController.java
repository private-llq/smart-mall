package com.jsy.controller;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.utils.ObjectMapperUtil;
import com.jsy.basic.util.utils.ValidatorUtils;
import com.jsy.dto.*;
import com.jsy.parameter.NewShopParam;
import com.jsy.parameter.NewShopSetParam;
import com.jsy.query.MainSearchQuery;
import com.jsy.service.INewShopService;
import com.jsy.domain.NewShop;
import com.jsy.query.NewShopQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.annotation.LoginIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/newShop")
@Slf4j
@Api(tags="新-商家")
public class NewShopController {
    @Autowired
    public INewShopService newShopService;


    /**
     * 保存和修改公用的
     * @param shopPacketParam  传递的实体
     * @return Ajaxresult转换结果
     */
    @ApiOperation("创建店铺")
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody NewShopParam shopPacketParam){
        log.info("入参：{}",shopPacketParam);
         ValidatorUtils.validateEntity(shopPacketParam,NewShopParam.newShopValidatedGroup.class);
        try {
            if (shopPacketParam.getId()==null){
                newShopService.addNewShop(shopPacketParam);
            }else {
                newShopService.update(shopPacketParam);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  CommonResult.error(-1,"创建失败！");
        }
        return  CommonResult.ok();
    }

    @LoginIgnore
    @PostMapping("/test")
    public R<Void> test(){
        return R.ok();
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @DeleteMapping(value="/{id}")
    @ApiOperation("删除店铺")
    public CommonResult delete(@PathVariable("id") Long id){
        try {
            newShopService.removeById(id);
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
    @GetMapping(value = "/get")
    @ApiOperation(("根据店铺id 查询店铺详情"))
    public CommonResult<NewShop> get(@RequestParam("id") Long id)
    {
        NewShop newShop = newShopService.getById(id);
        return CommonResult.ok(newShop);
    }


    /**
    * 返回list列表
    * @return
    */
//    @LoginIgnore
    @ApiOperation("查询所有店铺")
    @GetMapping(value = "/list")
    public CommonResult<List<NewShopDto>> list(){
        List<NewShop> list = newShopService.list(null);
        List<NewShopDto> newShopDtos = new ArrayList<>();
        for (NewShop newShop : list) {
            NewShopDto newShopDto = new NewShopDto();
            BeanUtils.copyProperties(newShop,newShopDto);
            newShopDtos.add(newShopDto);
        }
        return CommonResult.ok(newShopDtos);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<NewShop> json(@RequestBody NewShopQuery query)
    {
        Page<NewShop> page = new Page<NewShop>(query.getPage(),query.getRows());
        page = newShopService.page(page);
        return new PageList<NewShop>(page.getTotal(),page.getRecords());
    }

    @ApiOperation("通过店铺id预览店铺基本信息")
    @RequestMapping(value = "/getPreviewDto/{shopId}",method = RequestMethod.GET)
    public CommonResult<NewShopPreviewDto> getPreviewDto(@PathVariable("shopId") Long shopId){

   NewShopPreviewDto newShopPreviewDto = newShopService.getPreviewDto(shopId);

  return CommonResult.ok(newShopPreviewDto);

    }

    @ApiOperation("查询商家所拥有的的店铺信息")
    @RequestMapping(value = "/getOwnerShop/{ownerUuid}",method = RequestMethod.GET)
    public CommonResult<List<NewShopPreviewDto>> getOwnerShop(@PathVariable("ownerUuid") Long ownerUuid){
        List<NewShop> newShops = newShopService.list(new QueryWrapper<NewShop>().eq("owner_uuid", ownerUuid));
        List<NewShopPreviewDto> dtoList = new ArrayList<>();
        for (NewShop newShop : newShops) {
            NewShopPreviewDto newShopPreviewDto = new NewShopPreviewDto();
            BeanUtils.copyProperties(newShop,newShopPreviewDto);
            dtoList.add(newShopPreviewDto);
        }
        return CommonResult.ok(dtoList);
    }
    @ApiOperation("根据店铺id查询店铺设置")
    @RequestMapping(value = "/getSetShop/{shopId}",method = RequestMethod.GET)
    public CommonResult<NewShopSetDto> getSetShop(@PathVariable("shopId") Long shopId){
        NewShop newShop = newShopService.getById(shopId);
        NewShopSetDto newShopSetDto = new NewShopSetDto();
        BeanUtils.copyProperties(newShop,newShopSetDto);
        return CommonResult.ok(newShopSetDto);
    }

    @ApiOperation("根据店铺id修改店铺设置")
    @RequestMapping(value = "/setSetShop/",method = RequestMethod.POST)
    public CommonResult setSetShop(@RequestBody NewShopSetParam shopSetParam){
        try {
            newShopService.setSetShop(shopSetParam);
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return  CommonResult.error(-1,"修改失败！");
        }
    }

    /**************************************C端店铺的数据展示****************************************************************************/
    @ApiOperation("C端分类店铺列表")
    @RequestMapping(value = "/getShopAllList/",method = RequestMethod.POST)
    public CommonResult<List<NewShopRecommendDto>> getShopAllList(@RequestParam("treeId") Long treeId, @RequestParam("location")String location){
            List<NewShopRecommendDto> recommendDtoList = newShopService.getShopAllList(treeId,location);
            if (recommendDtoList!=null){
                return CommonResult.ok(recommendDtoList);
            }
            else {
                return new  CommonResult(-1,"失败",null);
            }

    }

    /**
     * 首页搜索
     * @param keyword 关键字
     * @param location 定位
     * @return
     */
    @ApiOperation("首页搜索")
    @GetMapping("mainSearch")
    public CommonResult<PageInfo<MyNewShopDto>> mainSearch(@RequestBody MainSearchQuery mainSearchQuery){
        PageInfo<MyNewShopDto> list = newShopService.mainSearch(mainSearchQuery);
        return CommonResult.ok(list);
    }


}
