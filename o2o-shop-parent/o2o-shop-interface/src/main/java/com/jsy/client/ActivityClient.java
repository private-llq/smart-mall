package com.jsy.client;
import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.Activity;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@FeignClient(value = "SERVICE-SHOP",configuration = FeignConfiguration.class)
public interface ActivityClient {

    //获取
    @ApiOperation("根据id获取店铺活动")
    @RequestMapping(value = "/activity/{uuid}",method = RequestMethod.GET)
    CommonResult<Activity> get(@PathVariable("uuid")String uuid);

    //获取所有店铺活动
    @ApiOperation("获取所有活动")
    @RequestMapping(value = "/activity/list/{uuid}",method = RequestMethod.GET)
    CommonResult<List<Activity>> getAll(@PathVariable("uuid")String uuid);


    @ApiModelProperty("根据消费金额查询符合条件活动")
    @RequestMapping(value = "/activity/pub/findOne",method = RequestMethod.POST)
    CommonResult<Activity> findOne(@RequestParam("shopUuid")String shopUuid,@RequestParam("money") BigDecimal money);

    /**
     * 查询正在进行的这一批满减活动
     */
    @ApiModelProperty("根据消费金额查询符合条件活动")
    @RequestMapping(value = "/activity/pub/runActivities",method = RequestMethod.POST)
    CommonResult<List<Activity>> runActivities(@RequestParam("shopUuid") String shopUuid);

    /**
     * 查询历史中最近的一批满减活动 状态：已撤销 进行中 已过期
     */
    @ApiModelProperty("根据消费金额查询符合条件活动")
    @RequestMapping(value = "/activity/pub/newestActivities",method = RequestMethod.POST)
    CommonResult<List<Activity>> newestActivities(@RequestParam("shopUuid") String shopUuid);

    /**
     * ids查询活动
     */
    @ApiOperation("ids查询活动")
    @PostMapping("/activity/pub/ActivitiesList")
    List<Activity> ActivitiesList(@RequestParam("activitiesIds") List<String> activitiesIds);

}
