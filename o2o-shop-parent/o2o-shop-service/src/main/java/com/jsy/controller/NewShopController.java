package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.GetServiceName;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.RestUtils;
import com.jsy.basic.util.utils.ValidatorUtils;
import com.jsy.clent.CommentClent;
import com.jsy.domain.Goods;
import com.jsy.dto.*;
import com.jsy.parameter.*;
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
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/newShop")
@Slf4j
@Api(tags="新-商家")
public class NewShopController {
    @Autowired
    public INewShopService newShopService;
    @Autowired
    private RestTemplate restTemplate;
    @Resource
    private CommentClent commentClent;


    /**
     * 创建店铺  基本信息填写
     * @param shopPacketParam  传递的实体
     * @return Ajaxresult转换结果
     */
    @ApiOperation("创建店铺基本信息填写")
    @PostMapping(value="/addBasic")
    public CommonResult addBasic(@RequestBody NewShopParam shopPacketParam){
        log.info("入参：{}",shopPacketParam);
         ValidatorUtils.validateEntity(shopPacketParam,NewShopParam.newShopValidatedGroup.class);
        try {
               Long shopId =  newShopService.addNewShop(shopPacketParam);
            return  CommonResult.ok(shopId);

        } catch (Exception e) {
            e.printStackTrace();
            return  CommonResult.error(-1,"创建失败！");
        }

    }

    /**
     * 创建店铺   资质认证填写
     * @param qualificationParam  传递的实体
     * @return Ajaxresult转换结果
     */
    @ApiOperation("创建店铺资质认证填写")
    @PostMapping(value="/addQualification")
    public CommonResult addQualification(@RequestBody NewShopQualificationParam qualificationParam){
        log.info("入参：{}",qualificationParam);
        ValidatorUtils.validateEntity(qualificationParam,NewShopParam.newShopValidatedGroup.class);
        try {
            newShopService.addQualification(qualificationParam);
        } catch (Exception e) {
            e.printStackTrace();
            return  CommonResult.error(-1,"资质上传失败！");
        }
        return  CommonResult.ok();
    }

    @ApiOperation("修改店铺基本信息填写")
    @PostMapping(value="/updateBasic")
    public CommonResult updateBasic(@RequestBody NewShopParam shopPacketParam){
        log.info("入参：{}",shopPacketParam);
        ValidatorUtils.validateEntity(shopPacketParam,NewShopParam.newShopValidatedGroup.class);
        try {
            newShopService.updateBasic(shopPacketParam);
            return  CommonResult.ok("修改成功");

        } catch (Exception e) {
            e.printStackTrace();
            return  CommonResult.error(-1,"修改失败！");
        }

    }

    /**
     * 修改所有店铺信息
     * @param modifyParam  传递的实体
     * @return Ajaxresult转换结果
     */
    @ApiOperation("修改所有店铺信息")
    @PostMapping(value="/updateNewShop")
    public CommonResult updateNewShop(@RequestBody NewShopModifyParam modifyParam){
        log.info("入参：{}",modifyParam);
        ValidatorUtils.validateEntity(modifyParam,NewShopParam.newShopValidatedGroup.class);
        try {
            newShopService.update(modifyParam);
        } catch (Exception e) {
            e.printStackTrace();
            return  CommonResult.error(-1,"修改店铺信息失败！");
        }
        return  CommonResult.ok();
    }
     /**
      * @author Tian
      * @since 2021/12/1-11:53
      * @description 预览门店基本信息
      **/
    @PostMapping(value = "/selectBasic")
    public CommonResult<NewShopBasicDto> selectBasic(@RequestParam("shopId") Long shopId){
       NewShopBasicDto basicDto =  newShopService.selectBasic(shopId);
        return CommonResult.ok(basicDto);
    }




    @LoginIgnore
    @PostMapping("/test")
    public R<Void> test(){
        return R.ok();
    }

    /**
    * 删除对象信息
    * @param shopId
    * @return
    */
    @DeleteMapping(value="/del")
    @ApiOperation("删除店铺")
    public CommonResult delete(@RequestParam("shopId") Long shopId){
        try {
            newShopService.removeById(shopId);
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return  CommonResult.error(-1,"删除失败！");
        }
    }

    /**
    * 根据id查询一条
    * @param shopId
    */
    @GetMapping(value = "/get")
    @ApiOperation(("根据店铺id 查询店铺详情"))
    public CommonResult<NewShopDto> get(@RequestParam("shopId") Long shopId)
    {
        NewShop newShop = newShopService.getById(shopId);
        if (newShop==null){
            return  new CommonResult(-1,"店铺为空",null);
        }
        NewShopDto newShopDto = new NewShopDto();
        BeanUtils.copyProperties(newShop,newShopDto);
        return CommonResult.ok(newShopDto);
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

    @ApiOperation("通过店铺id预览店铺基本信息")
    @RequestMapping(value = "/getPreviewDto",method = RequestMethod.GET)
    public CommonResult<NewShopPreviewDto> getPreviewDto(@RequestParam("shopId") Long shopId){
        NewShopPreviewDto newShopPreviewDto = newShopService.getPreviewDto(shopId);
        return CommonResult.ok(newShopPreviewDto);

    }

    @ApiOperation("查询商家所拥有的的店铺信息")
    @RequestMapping(value = "/getOwnerShop",method = RequestMethod.GET)
    public CommonResult<List<NewShopPreviewDto>> getOwnerShop(@RequestParam("ownerUuid") Long ownerUuid){
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
    @RequestMapping(value = "/getSetShop",method = RequestMethod.GET)
    public CommonResult<NewShopSetDto> getSetShop(@RequestParam("shopId") Long shopId){
        NewShop newShop = newShopService.getById(shopId);
        NewShopSetDto newShopSetDto = new NewShopSetDto();
        BeanUtils.copyProperties(newShop,newShopSetDto);
        SelectShopCommentScoreDto data = commentClent.selectShopCommentScore(shopId).getData();
        newShopSetDto.setScore(data.getScore());
        newShopSetDto.setSize(data.getSize());
        return CommonResult.ok(newShopSetDto);
    }

    @ApiOperation("根据店铺id修改店铺设置")
    @RequestMapping(value = "/setSetShop",method = RequestMethod.POST)
    public CommonResult setSetShop(@RequestBody NewShopSetParam shopSetParam){
        try {
            newShopService.setSetShop(shopSetParam);
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return  CommonResult.error(-1,"修改失败！");
        }
    }

     /**
      * @author Tian
      * @since 2021/12/3-9:57
      * @description 查询店铺支持
      **/
     @ApiOperation("查询店铺支持")
     @RequestMapping(value = "/getSupport",method = RequestMethod.GET)
     public CommonResult getSupport(@RequestParam("shopId") Long shopId){
         try {
             NewShopSupportDto suportDto = newShopService.getSupport(shopId);
             return CommonResult.ok(suportDto);
         } catch (Exception e) {
             e.printStackTrace();
             return  CommonResult.error(-1,"修改失败！");
         }
     }


    /**************************************C端店铺的数据展示****************************************************************************/
    @ApiOperation("C端分类店铺列表")
    @RequestMapping(value = "/getShopAllList",method = RequestMethod.POST)
    public CommonResult<PageInfo<NewShopRecommendDto>> getShopAllList(@RequestBody NewShopQuery shopQuery){
            PageInfo<NewShopRecommendDto> recommendDtoList = newShopService.getShopAllList(shopQuery);
            if (recommendDtoList!=null){
                return CommonResult.ok(recommendDtoList);
            }
            else {
                return new  CommonResult(-1,"失败",null);
            }
    }

    @ApiOperation("C端查询店铺的距离多远")
    @PostMapping("/getDistance")
    public CommonResult<NewShopDistanceDto> getDistance(@RequestBody NewShopDistanceParam distanceParam){
        NewShopDistanceDto distanceDto =  newShopService.getDistance(distanceParam);
        return CommonResult.ok(distanceDto);
    }


    /**
     * 首页搜索
     * @param mainSearchQuery
     * @return
     */
    @ApiOperation("首页搜索")
    @PostMapping("mainSearch")
    public CommonResult<PageInfo<MyNewShopDto>> mainSearch(@RequestBody MainSearchQuery mainSearchQuery){
        PageInfo<MyNewShopDto> list = newShopService.mainSearch(mainSearchQuery);
        return CommonResult.ok(list);
    }

    @ApiOperation("根据id批量查询")
    @PostMapping("/batchIds")
    public CommonResult<List<NewShopDto>> batchIds(@RequestBody List<Long> ids)
    {
        List<NewShopDto> dtoList = newShopService.batchIds(ids);
        return CommonResult.ok(dtoList);
    }





    /**************************************大后台数据展示****************************************************************************/
//    @ApiOperation("店铺展示列表")
//    @RequestMapping(value = "/newShopPage",method = RequestMethod.POST)
//    public CommonResult<PageInfo<NewShopDto>> newShopPage(@RequestBody NewShopQuery shopQuery){
//        PageInfo<NewShopDto> shopAllList = newShopService.newShopPage(shopQuery);
//        if (shopAllList!=null){
//            return CommonResult.ok(shopAllList);
//        }
//        else {
//            return new  CommonResult(-1,"失败",null);
//        }
//    }
//    @ApiOperation("店铺审核")
//    @RequestMapping(value = "/newShopState",method = RequestMethod.POST)
//    public CommonResult newShopState(@RequestParam("shopId") Long shopId,@RequestParam("state") Integer state){
//        NewShop newShop = newShopService.getById(shopId);
//        newShop.setState(state);
//        boolean b = newShopService.update(newShop, null);
//        if (b){
//            return  CommonResult.ok();
//        }else {
//            return new CommonResult(-1,"审核失败",null);
//        }
//    }
    @ApiOperation("本月入驻的商家数量")
    @RequestMapping(value = "/newShopAudit",method = RequestMethod.POST)
    public CommonResult newShopAudit(){
        Integer count = newShopService.newShopAudit();
        return CommonResult.ok(count);
    }


}
