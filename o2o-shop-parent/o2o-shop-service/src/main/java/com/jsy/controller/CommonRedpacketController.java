package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.CommonRedpacket;
import com.jsy.query.CommonRedpacketQuery;
import com.jsy.service.ICommonRedpacketService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/commonRedpacket")
@Api(tags = "通用红包模块")
public class CommonRedpacketController {

    @Autowired
    public ICommonRedpacketService commonRedpacketService;

    /**
    * 保存和修改公用的
    * @param commonRedpacket  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    @ApiOperation(value = "1.01 新增通用红包任务", httpMethod = "POST")
    public CommonResult save(@RequestBody CommonRedpacket commonRedpacket){
        commonRedpacketService.saveAndUpdate(commonRedpacket);
        return CommonResult.ok();
    }

    /**
    * 未使用的红包可删除对象信息
    * @return
    */
    @RequestMapping(value="/{uuid}",method=RequestMethod.DELETE)
    @ApiOperation(value = "1.02 删除未启用的红包活动", httpMethod = "POST")
    public CommonResult delete(@PathVariable("uuid") String uuid){
        commonRedpacketService.deleteById(uuid);
        return CommonResult.ok();
    }

    //获取
    @RequestMapping(value = "/{uuid}",method = RequestMethod.GET)
    @ApiOperation(value = "1.03 通过id获取通用红包任务信息", httpMethod = "POST")
    public CommonResult getCommonRedpacket(@PathVariable("uuid")String uuid) {
        return  CommonResult.ok(commonRedpacketService.getByUuid(uuid));
    }

    //领取红包
    @RequestMapping(value = "/grant/{uuid}",method = RequestMethod.GET)
    @ApiOperation(value = "1.04 用户领取红包时通用红包判断是否可领取及删减红包数量", httpMethod = "POST")
    public CommonResult grantCommonRedpacket(@PathVariable("uuid")String uuid) {
        return CommonResult.ok(commonRedpacketService.grant(uuid));
    }

    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    @ApiOperation(value = "1.05 分页查询通用红包", httpMethod = "POST")
    public PageList<CommonRedpacket> json(@RequestBody CommonRedpacketQuery query) {
        Page<CommonRedpacket> page = new Page<CommonRedpacket>(query.getPage(),query.getRows());
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("deleted",query.getDeleted());
        page = commonRedpacketService.page(page,queryWrapper);
        return new PageList<CommonRedpacket>(page.getTotal(),page.getRecords());
    }

    //获取
    @PostMapping("/getMapByUuid")
    @ApiOperation(value = "获取红包信息通过uuids", httpMethod = "POST")
    public Map<String,CommonRedpacket> getMapByUuid(@RequestBody List<String> uuids) {
        return commonRedpacketService.getMapByUuid(uuids);
    }
}
