package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.domain.OrderLog;
import com.jsy.query.OrderLogQuery;
import com.jsy.service.IOrderLogService;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orderLog")
@Api("订单日志")
public class OrderLogController {


    @Autowired
    public IOrderLogService orderLogService;

    /**
    * 保存和修改公用的
    * @param orderLog  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public CommonResult save(@RequestBody OrderLog orderLog){
        boolean save = orderLogService.save(orderLog);
        if(save) {return CommonResult.ok();}
        else { return CommonResult.error(-1, "订单日志生成失败"); }
    }

    @RequestMapping(value = "/finish/{id}",method = RequestMethod.GET)
    public CommonResult finish(@PathVariable("id")Integer id){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        orderLogService.updateByUid(id,userEntity.getUid());
        return CommonResult.ok();
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public CommonResult delete(@PathVariable("id") Long id){
        orderLogService.removeById(id);
        return CommonResult.ok();

    }

    //获取
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public CommonResult get(@PathVariable("id")Long id) {
        return CommonResult.ok(orderLogService.getById(id));
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public CommonResult pageList(@RequestBody OrderLogQuery query){

        QueryWrapper<OrderLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotEmpty(query.getType()),"type",query.getType())
                .eq(StringUtils.isNotBlank(query.getRequestIp()),"request_ip",query.getRequestIp())
                .eq(StringUtils.isNotBlank(query.getFinishPersonUuid()),"finish_person_uuid",query.getFinishPersonUuid())
                .ge(query.getCreateTime1()!=null,"create_time",query.getCreateTime1())
                .le(query.getCreateTime2()!=null,"create_time",query.getCreateTime2());
        Page<OrderLog> orderLogPage = new Page<>(query.getPage(), query.getRows());
        IPage<OrderLog> logs = orderLogService.pageList(orderLogPage,queryWrapper);


        return CommonResult.ok(new PageList<OrderLog>(logs.getTotal(),logs.getRecords()));
    }
}
