package com.jsy.controller;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.PlatformActivity;
import com.jsy.query.PlatformActivityQuery;
import com.jsy.service.IPlatformActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/platformActivity")
@Api(tags = "平台活动模块")
public class PlatformActivityController {

    @Autowired
    public IPlatformActivityService platformActivityService;

    /**
    * 保存和修改公用的
    * @param platformActivity  传递的实体
    * @return CommonResult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    @ApiOperation("新增活动，修改活动")
    public CommonResult<Boolean> save(@RequestBody PlatformActivity platformActivity){
        platformActivityService.saveAndUpdate(platformActivity);
        return CommonResult.ok();
    }

    /**
    * 删除对象信息
    * @param uuid
    * @return
    */
    @ApiOperation("根据id删除活动")
    @RequestMapping(value="/{uuid}",method=RequestMethod.DELETE)
    public CommonResult<Boolean> delete(@ApiParam("活动id") @PathVariable("uuid") String uuid){
         platformActivityService.delete(uuid);
         return CommonResult.ok();
    }

    /**
     * 根据活动id获取活动信息
     * @param uuid
     * @return 该id的活动信息
     */
    @ApiOperation("根据id获取活动信息")
    @RequestMapping(value = "/{uuid}",method = RequestMethod.GET)
    public CommonResult<PlatformActivity> get(@PathVariable("uuid")String uuid){
        return CommonResult.ok(platformActivityService.getByUuid(uuid));
    }


    /**
    * 根据条件查询活动信息
    * @return
    */
    @ApiOperation("根据条件查看活动信息")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public CommonResult<PageList<PlatformActivity>> list(@RequestBody PlatformActivityQuery<PlatformActivity> activity){
        return CommonResult.ok(platformActivityService.selectByConditon(activity));
    }

}
