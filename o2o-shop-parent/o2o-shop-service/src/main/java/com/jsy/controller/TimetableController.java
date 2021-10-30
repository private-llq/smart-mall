package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.service.ITimetableService;
import com.jsy.domain.Timetable;
import com.jsy.query.TimetableQuery;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/timetable")
@Api(tags = "抢购时间controller")
public class TimetableController {

    @Autowired
    public ITimetableService timetableService;

    /**
    * 新增一个时间
    * @param timetable  传递的实体
    * @return CommonResult 转换结果
    */
    @ApiOperation("新增一个时间")
    @RequestMapping(value="/pub/save",method= RequestMethod.POST)
    public CommonResult save(@RequestBody Timetable timetable){
        timetableService.add(timetable);
        return CommonResult.ok();
    }

    /**
    * 根据uuid删除一个时间
    * @param uuid
    * @return
    */
    @ApiOperation("根据uuid删除一个时间")
    @RequestMapping(value="/pub/{uuid}",method=RequestMethod.DELETE)
    public CommonResult delete(@PathVariable("uuid") String uuid){
        timetableService.deleteOne(uuid);
        return CommonResult.ok();
    }

    /**
     * 修改一个时间的状态
     * @param status
     * @return
     */
    @ApiOperation("修改一个时间的状态")
    @RequestMapping(value = "/pub/{status}/{uuid}",method = RequestMethod.PUT)
    public CommonResult update(@PathVariable("status")Long status,@PathVariable("uuid")String uuid) {
        timetableService.updateStatus(uuid,status);
        return CommonResult.ok();
    }


    /**
    * 根据条件分页 查看 时间
    * @return
    */
    @ApiOperation("根据条件分页 查看 时间")
    @RequestMapping(value = "/pub/list",method = RequestMethod.POST)
    public CommonResult<PagerUtils<Timetable>> list(@RequestBody TimetableQuery query){
        PagerUtils<Timetable> select = timetableService.selectByConditon(query);
        return CommonResult.ok(select);
    }


    /**
    * 查询当前可以使用的时间
    *
    * @param // 查询对象
    * @return PageList 分页对象
    */
    @ApiOperation("查询当前可以使用的时间")
    @RequestMapping(value = "/pub/getAll",method = RequestMethod.GET)
    public CommonResult<List<Timetable>> getAll() {
        List<Timetable> selectOk = timetableService.selectOk();
        return CommonResult.ok(selectOk);
    }


    /**
     * 查询这批活动的时长
     */
    @ApiOperation("查询这批活动的时长")
    @RequestMapping(value = "/pub/selectTimeLong",method = RequestMethod.GET)
    public CommonResult<Timetable> selectTimeLong(){
        Timetable timetable = timetableService.getOne(new QueryWrapper<Timetable>().eq("state", 2));
        return CommonResult.ok(timetable);
    }

}
