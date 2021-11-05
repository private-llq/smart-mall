package com.jsy.controller;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.basic.util.utils.ValidatorUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.ShopInfo;
import com.jsy.dto.*;
import com.jsy.parameter.*;
import com.jsy.query.AdminShopQuery;
import com.jsy.query.ShopInfoQuery;
import com.jsy.service.IShopInfoService;
import com.jsy.vo.ShopInfoParamVo;
import com.jsy.vo.ShopInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/shopInfo")
@Api(tags = "店铺信息模块")
@Slf4j
public class ShopInfoController {
    @Autowired
    public IShopInfoService shopInfoService;
    @ApiOperation("申请添加门店")
    @RequestMapping(value="/applyShop",method= RequestMethod.POST)
    public CommonResult<Boolean> applyShop(@RequestBody ShopInfoParamVo shopInfoParam){
        ValidatorUtils.validateEntity(shopInfoParam,ShopInfoParamVo.class);
        System.out.println("接收到"+shopInfoParam);
        log.info("入参：{}",shopInfoParam);
        Boolean aBoolean = shopInfoService.applyShop(shopInfoParam);
        if(aBoolean){
            return new CommonResult(200,"申请成功",aBoolean);
        }else{
            return new CommonResult(-1,"申请失败",aBoolean);
        }
    }


    @ApiOperation("分页查询待审核的店铺")
    @RequestMapping(value="/selectShopInfoByUnrevised",method= RequestMethod.POST)
    public CommonResult<IPage> selectShopInfoByUnrevised(@RequestBody PageParam pageParam){
        IPage<ShopInfo> shopInfoIPage = shopInfoService.selectShopInfoByUnrevised(pageParam);
        return new CommonResult(200,"查询成功",shopInfoIPage);
    }
    @ApiOperation("批量审核店铺申请")
    @RequestMapping(value="/approveShop",method= RequestMethod.POST)
    public  CommonResult<Boolean> approveShop(@RequestBody List<String> uuidS) {
        Boolean aBoolean = shopInfoService.approveShop(uuidS);
        if(aBoolean){
            return new CommonResult(200,"审批成功",true);
        }
        return new CommonResult(-1,"操作失败",false);
    }
    @ApiOperation("根据账户查看自己的门店列表")
    @RequestMapping(value="/selectShopListByShopOwnerUuid",method= RequestMethod.POST)
    public CommonResult< List<ShopInfoByOwnerParam>> selectShopListByShopOwnerUuid() {
        List<ShopInfoByOwnerParam> shopInfoByOwnerParams = shopInfoService.selectShopListByShopOwnerUuid();
        return new CommonResult(200,"操作成功",shopInfoByOwnerParams);
    }

    @ApiOperation("删除没有审核通过的其他店铺")
    @RequestMapping(value="/deleteShopInfoByUuid/{uuid}",method= RequestMethod.DELETE)
    public  CommonResult< Boolean> deleteShopInfo(@PathVariable("uuid") String uuid) {
        Boolean aBoolean = shopInfoService.deleteShopInfo(uuid);
        return new CommonResult(200,"删除成功",aBoolean);
    }

    @ApiOperation("查询删除的店铺列表")
    @RequestMapping(value="/selectDeletedShopList",method= RequestMethod.GET)
    public  CommonResult< List<ShopInfoByOwnerParam>> selectDeletedShopList( ) {
        List<ShopInfoByOwnerParam> shopInfoByOwnerParams = shopInfoService.selectDeletedShopList();
        return new CommonResult(200,"查询成功",shopInfoByOwnerParams);
    }

    @ApiOperation("查询店铺信息")
    @RequestMapping(value="/selectShopMessage",method= RequestMethod.GET)
    public CommonResult<ShopMessageDto> selectShopMessage(@RequestParam("shopUuid")String shopUuid){
        ShopMessageDto shopMessageDto = shopInfoService.selectShopMessage(shopUuid);
        return new CommonResult(200,"查询成功",shopMessageDto);
    }

    @ApiOperation("查询店铺资料")
    @RequestMapping(value="/selectShopMeans",method= RequestMethod.GET)
    public CommonResult<SelectShopMeansDto>selectShopMeans(@RequestParam("shopUuid") String shopUuid){
        SelectShopMeansDto selectShopMeansDto = shopInfoService.selectShopMeans(shopUuid);
        return new CommonResult(200,"查询成功",selectShopMeansDto);
    }

    @ApiOperation("修改店铺公告")
    @RequestMapping(value="/updateNotice",method= RequestMethod.POST)
    public CommonResult<Boolean> updateNotice(@RequestBody  UpdateNoticeParam updateNoticeParam){
        Boolean aBoolean = shopInfoService.updateNotice(updateNoticeParam);
        if(aBoolean){
            return new CommonResult(200,"修改成功",aBoolean);
        }
        return new CommonResult(200,"修改失败",aBoolean);

    }
    @ApiOperation("修改店铺环境")
    @RequestMapping(value="/updateShopEnvironment",method= RequestMethod.POST)
    public CommonResult<Boolean> updateShopEnvironment(@RequestBody  UpdateShopEnvironmentParam updateShopEnvironmentParam){
        Boolean aBoolean = shopInfoService.updateShopEnvironment(updateShopEnvironmentParam);
        if(aBoolean){
            return new CommonResult(200,"修改成功",aBoolean);
        }
        return new CommonResult(200,"修改失败",aBoolean);
    }


    @ApiOperation("修改营业状态")
    @RequestMapping(value="/updateBusinessStatus",method= RequestMethod.POST)
    public CommonResult<Boolean> updateBusinessStatus(@RequestBody UpdateBusinessStatusParam updateBusinessStatusParam){
        Boolean aBoolean = shopInfoService.updateBusinessStatus(updateBusinessStatusParam);
        if(aBoolean){
            return new CommonResult(200,"修改成功",aBoolean);
        }
        return new CommonResult(200,"修改失败",aBoolean);
    }
    @ApiOperation("修改接单电话")
    @RequestMapping(value="/updateOrderReceivingPhone",method= RequestMethod.POST)
    public CommonResult<Boolean> updateOrderReceivingPhone(@RequestBody  UpdateOrderReceivingPhoneParam updateOrderReceivingPhoneParam){
        Boolean aBoolean = shopInfoService.updateOrderReceivingPhone(updateOrderReceivingPhoneParam);
        if(aBoolean){
            return new CommonResult(200,"修改成功",aBoolean);
        }
        return new CommonResult(200,"修改失败",aBoolean);

    }

    @ApiOperation("修改店铺单笔配送费")
    @RequestMapping(value="/updatePostage",method= RequestMethod.POST)
    public CommonResult<Boolean> updatePostage(@RequestBody  UpdatePostageParam updatePostageParam) {
        Boolean aBoolean = shopInfoService.updatePostage(updatePostageParam);
        if (aBoolean) {
            return new CommonResult(200,"修改成功",aBoolean);
        }
        return new CommonResult(-1,"修改失败",aBoolean);

    }

    @ApiOperation("查询商家单笔配送费")
    @RequestMapping(value="/selectPostage",method= RequestMethod.GET)
    public CommonResult<BigDecimal> selectPostage(@RequestParam("shopUuid")String shopUuid) {
        BigDecimal postage = shopInfoService.selectPostage(shopUuid);
        return new CommonResult(200,"查询成功",postage);
    }
/******************************************************************************************/

    /**
     * 商家后台设置 列表展示 1  大图展示 2
     */
    @ApiOperation("商家后台设置商品是列表展示，还是大图展示")
    @RequestMapping(value="/pub/setListAndPic",method= RequestMethod.POST)
    public CommonResult setListAndPic(@RequestParam String shopUuid,@RequestParam Integer state){
        shopInfoService.update(new UpdateWrapper<ShopInfo>().eq("uuid",shopUuid).set(Objects.nonNull(state),"is_list_picture",state));
        return CommonResult.ok();
    }

    /**
    * 保存
    * @param shopInfoVo  传递的实体
    * @return
    */
    @ApiOperation("新增注册店铺信息")
    @RequestMapping(value="/pub/save",method= RequestMethod.POST)
    public CommonResult<Boolean> save(@RequestBody ShopInfoVo shopInfoVo){
        shopInfoService.addShop(shopInfoVo);
        return CommonResult.ok();
    }

    /**
     * 商家修改店铺基本信息
     * @param shopInfoVo
     * @return
     */
    @ApiOperation("[商家]修改店铺信息")
    @PutMapping("/")
    public CommonResult<Boolean> update(@RequestBody ShopInfoVo shopInfoVo) {
        return shopInfoService.updateShop(shopInfoVo) > 0 ? CommonResult.ok() : CommonResult.error(-1,"操作失败");
    }

    /**
    * 商家退出平台
    * @param uuid
    * @return
    */
    @ApiOperation("[商家]根据uuid关闭店铺")
    @RequestMapping(value="/{uuid}",method=RequestMethod.DELETE)
    public CommonResult<Boolean> delete(@PathVariable("uuid") String uuid){
        shopInfoService.closeShop(uuid);
        return CommonResult.ok();
    }
    /**
     * 商家店铺基本信息总览
     * @return
     */
    @ApiOperation("[商家]商品信息总览")
    @GetMapping("/overview")
    public CommonResult<GoodsOverViewDTO> overview() {
        GoodsOverViewDTO overview = shopInfoService.overview();
        return CommonResult.ok(overview);
    }


    /**
     * 用户根据uuid 查看店铺基本信息
     * @param uuid
     * @return
     */
    @ApiOperation("[用户]根据uuid查看店铺信息")
    @RequestMapping(value = "pub/get/{uuid}",method = RequestMethod.GET)
    public CommonResult<ShopInfo> get(@PathVariable("uuid")String uuid) {
        return CommonResult.ok(shopInfoService.getOne(new QueryWrapper<ShopInfo>().eq("uuid",uuid)));
    }

    @ApiOperation("[运营商]审核店铺信息")
    @RequestMapping(value = "/changeShopStatus/{uuid}/{status}",method = RequestMethod.GET)
    public CommonResult<Boolean> changeStatus(@PathVariable("uuid") String uuid,@PathVariable("status") String status) {
        return shopInfoService.changeStatus(uuid,status) > 0 ? CommonResult.ok() : CommonResult.error(-1,"操作失败");
    }

    // TODO: 2020/12/18 暂时不做登录才有权限查看 等待大平台
    @ApiOperation("[运营商]条件查看所有店铺")
    @PostMapping("/pub/admin/list")
    public CommonResult<PagerUtils<ShopInfo>> getList(@RequestBody AdminShopQuery adminShopQuery) {
        return CommonResult.ok(shopInfoService.getList(adminShopQuery));
    }

    @ApiOperation("【用户查看店铺】")
    @GetMapping("/pub/{uuid}/{longitude}/{latitude}")
    public CommonResult<ShopInfoDTO> selectShop(@ApiParam("店铺uuid") @PathVariable("uuid") String uuid,
                                                @ApiParam("定位经度") @PathVariable("longitude") double longitude,
                                                @ApiParam("定位纬度") @PathVariable("latitude") double latitude) {
        return CommonResult.ok(shopInfoService.selectShop(uuid,longitude,latitude));
    }

    @ApiOperation("[首页]根据关键字搜索店铺")
    @PostMapping("/pub/selectByCondition")
    public CommonResult<PagerUtils<ShopQueryDTO>> selectByCondition(@RequestBody ShopInfoQuery shopInfoQuery) {
        PagerUtils<ShopQueryDTO> shopQueryDTOPagerUtils = shopInfoService.selectByConditon(shopInfoQuery);
        return CommonResult.ok(shopQueryDTOPagerUtils);
    }

    /**
     * 根据用户的评价，修改店铺整体的星级
     * @param shopUuid
     * @param star
     * @return
     */
    @ApiOperation("修改店铺星级")
    @GetMapping("/pub/updateShopStar/{shopUuid}/{star}")
    public CommonResult<Boolean> updateShopStar(@PathVariable("shopUuid") String shopUuid ,@PathVariable("star") double star) {
        shopInfoService.selectShopStar(shopUuid,star);
        return CommonResult.ok();
    }

    //TODO: 2020/12/15 根据范围推荐四家按星级
    @ApiOperation("首页推荐")
    @PostMapping("/pub/commend")
    public CommonResult<List<ShopQueryDTO>> commendShop(@RequestBody ShopInfoQuery query) {
        return CommonResult.ok(shopInfoService.commendShop(query));
    }

    /**
     * 满减活动页面 推荐
     * @param query
     * @return
     */
    @ApiOperation("满减店铺推荐页面")
    @PostMapping("/pub/active")
    public CommonResult<PagerUtils<ShopActiveDTO>> getActivityShop(@RequestBody ShopInfoQuery query) {
        return CommonResult.ok(shopInfoService.getActivity(query));
    }

    //获取
    @PostMapping("/getMapByUuid")
    @ApiOperation(value = "获取红包信息通过uuids", httpMethod = "POST")
    public Map<String,ShopInfo> getShopInfoMapByUuid(@RequestBody List<String> uuids) {
        return shopInfoService.getMapByUuid(uuids);
    }


}
