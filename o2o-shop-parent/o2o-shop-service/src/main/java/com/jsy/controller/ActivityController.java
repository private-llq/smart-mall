package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.Activity;
import com.jsy.query.ActivityQuery;
import com.jsy.service.IActivityService;
import com.jsy.vo.ActivityVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/activity")
@Api(tags = "店铺活动")
public class ActivityController {
    @Autowired
    public IActivityService activityService;
    /**
    * 商家端 创建满减活动 888888
    * @return CommonResult转换结果
    */
    //改vo
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public CommonResult<Boolean> saveList(@RequestBody ActivityVo activityVo){
        activityService.saveList(activityVo);
        return  CommonResult.ok() ;
    }


    /**
    * 用户端-满减页面-查询店铺活动
    * @return
    */
    @ApiOperation("根据条件查询店铺活动")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public CommonResult<PageList<Activity>> list(@RequestBody ActivityQuery<Activity> query){
        return CommonResult.ok(activityService.selectByConditon(query));
    }

    @ApiModelProperty("根据消费金额查询符合条件活动")
    @RequestMapping(value = "/pub/findOne",method = RequestMethod.POST)
    public CommonResult<Activity> findOne(@RequestParam("shopUuid")String shopUuid,@RequestParam("money") BigDecimal money) {
        Activity one = activityService.findOne(shopUuid, money);
        return CommonResult.ok(one);
    }

    /**
     *商家端 查询一个商家正在进行的这一批满减活动
     */
    @ApiModelProperty("查询正在进行的这一批满减活动")
    @RequestMapping(value = "/pub/runActivities",method = RequestMethod.POST)
    public CommonResult<List<Activity>> runActivities(@RequestParam String shopUuid) {
        List<Activity> activities = activityService.runActivities(shopUuid);
        return CommonResult.ok(activities);
    }
    /**
     *商家端 查询历史中最近的一批满减活动 （包含：已撤销 进行中 已过期）
     */
    @ApiOperation("查询历史中最近的一批满减活动")
    @RequestMapping(value = "/pub/newestActivities",method = RequestMethod.POST)
    public CommonResult<List<Activity>> newestActivities(@RequestParam String shopUuid) {
        List<Activity> activities = activityService.newestActivities(shopUuid);
        return CommonResult.ok(activities);
    }
    /**
     * 撤销活动
     */
    @ApiOperation("撤销活动")
    @GetMapping("/pub/revokeActivities")
    public CommonResult revokeActivities(@RequestParam String shopUuid) {
        activityService.update(new UpdateWrapper<Activity>().eq("shop_uuid",shopUuid).eq("deleted",1).set("deleted",0).set("revoke_time",LocalDateTime.now()));
        return CommonResult.ok();
    }

    /**
     * ids查询活动
     */
    @ApiOperation("ids查询活动")
    @PostMapping("/pub/ActivitiesList")
        public List<Activity> ActivitiesList(@RequestParam("activitiesIds") List<String> activitiesIds) {
        List<Activity> activityList = activityService.list(new QueryWrapper<Activity>().in("uuid", activitiesIds));
        return activityList;
    }


}
