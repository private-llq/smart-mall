package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.domain.ShopApplySeckill;
import com.jsy.domain.Timetable;
import com.jsy.dto.HashMapDto;
import com.jsy.dto.ShopApplySeckillDTO;
import com.jsy.dto.ShopApplySeckillDTOShow;
import com.jsy.mapper.TimetableMapper;
import com.jsy.query.ShopApplySeckillQuery;
import com.jsy.query.UserApplySeckillQuery;
import com.jsy.service.IShopApplySeckillService;
import com.jsy.vo.ShopApplySeckillVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.TreeMap;


@RestController
@RequestMapping("/shopApplySeckill")
@Api(tags ="商品秒杀活动模块")
public class ShopApplySeckillController {

    @Autowired
    private IShopApplySeckillService shopApplySeckillService;

    @Autowired
    private TimetableMapper timetableMapper;


    /**
     * 修改库存  库存-购买数量
     */
    @RequestMapping(value = "pub/updateStock/{shopUuid}/{goodsUuid}/{num}",method = RequestMethod.POST)
    public CommonResult updateStock(@PathVariable("shopUuid") String shopUuid,
                                               @PathVariable("goodsUuid") String goodsUuid,
                                               @PathVariable("num") Integer cartNum){
        boolean update = shopApplySeckillService.updateStock(shopUuid,goodsUuid,cartNum);

        return CommonResult.ok(update);

    }


    /**
     * 平台端
     * 分页查询所有秒杀活动申请
     * @param query 查询对象
     * @return PageList 分页对象
     */
    @ApiOperation("平台管理者 分页查看所有秒杀活动申请")
    @RequestMapping(value = "pub/pageList",method = RequestMethod.POST)
    public CommonResult ApplicationPageList(@RequestBody ShopApplySeckillQuery query)
    {
        PageList<ShopApplySeckillDTO> list = shopApplySeckillService.ApplicationPageList(query);
        return CommonResult.ok(list);
    }

    /**
     * 商家端
     * 商家提交秒杀活动申请
     */
    @ApiOperation("商家端 商家提交秒杀活动申请")
    @RequestMapping(value="/submitApplication",method= RequestMethod.POST)
    public CommonResult submitApplication(@RequestBody ShopApplySeckill ShopApplySeckill){
        UserDto currentUser = CurrentUserHolder.getCurrentUser();
        String shopUuid = currentUser.getUuid();
        ShopApplySeckill.setShopUuid(shopUuid);
        shopApplySeckillService.submitApplication(ShopApplySeckill);
        return CommonResult.ok("已提交");
    }

    /**
     *
     * 平台审核通过
     * 传入申请表uuid
     */
    @ApiOperation("平台审核-->通过")
    @RequestMapping(value="/pub/reviewApplication/{uuid}",method= RequestMethod.GET)
    public CommonResult reviewApplication (@PathVariable("uuid") String uuid){
        shopApplySeckillService.reviewApplication(uuid);
        return CommonResult.ok();
    }

    /**
     * 平台审核驳回
     * 传入申请表uuid
     */
    @ApiOperation("平台审核-->驳回,原因")
    @RequestMapping(value="/pub/rejectApplication",method= RequestMethod.POST)
    public CommonResult rejectApplication (@RequestBody ShopApplySeckillVo ShopApplySeckillVo){
        String uuid = ShopApplySeckillVo.getUuid();
        String rejectionReasons = ShopApplySeckillVo.getRejectionReasons();
        shopApplySeckillService.rejectApplication(uuid,rejectionReasons);
        return CommonResult.ok();
    }

    /**
     * 用户端
     * 根据时间段查询 平台审核通过的活动  （首页正在抢活动展示）
     */
    @ApiOperation("用户端 商城首页展示正在抢购的活动")
    @RequestMapping(value = "/pub/userPageList",method = RequestMethod.POST)
    public CommonResult userApplicationPageList(@RequestBody UserApplySeckillQuery query)
    {
        ShopApplySeckillDTOShow shopApplySeckillDTOShow = shopApplySeckillService.userApplicationPageList(query);
        return CommonResult.ok(shopApplySeckillDTOShow);
    }


    /**
     * 用户端
     * 根据时间段查询 平台审核通过的活动 （主活动页面）
     */
    @ApiOperation("用户端 秒杀主活动页面展示")
    @RequestMapping(value = "/pub/userList",method = RequestMethod.POST)
    public CommonResult userApplicationList()
    {
        Timetable timetable = timetableMapper.selectOne(new QueryWrapper<Timetable>().eq("state", 2));
        LocalTime localTime = timetable.getTime();
        LocalDateTime endTime = LocalDateTime.of(LocalDate.now(), localTime);
        TreeMap<LocalTime, List<ShopApplySeckillDTO>> hashMap = shopApplySeckillService.activityList();
        HashMapDto hashMapDto = new HashMapDto();
        hashMapDto.setData(hashMap);
        hashMapDto.setTime(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8")));
        hashMapDto.setEndTime(endTime.toEpochSecond(ZoneOffset.of("+8")));
        return CommonResult.ok(hashMapDto);
    }

    /**
     * 查询一个商品的秒杀活动信息
     */
    @ApiOperation("查询一个商品的秒杀活动信息")
    @RequestMapping(value = "/pub/selectKillInfo/{shopUuid}/{goodsUuid}",method = RequestMethod.POST)
    public CommonResult<ShopApplySeckill> selectKillInfo(@PathVariable("shopUuid") String shopUuid ,@PathVariable("goodsUuid") String goodsUuid){
        ShopApplySeckill selectKillInfo= shopApplySeckillService.selectKillInfo(shopUuid,goodsUuid);
        return CommonResult.ok(selectKillInfo);
    }

}
