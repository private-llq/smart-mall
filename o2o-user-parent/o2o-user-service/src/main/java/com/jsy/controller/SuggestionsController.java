package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.Suggestions;
import com.jsy.query.SuggestionsQuery;
import com.jsy.service.ISuggestionsService;
import com.jsy.vo.SuggestionsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/pub/suggestions")
@Api(tags = "建议收集")
public class SuggestionsController {
    @Autowired
    public ISuggestionsService suggestionsService;
    /**
    * 保存和修改公用的
    * @param suggestionsvo  传递的实体
    * @return Ajaxresult转换结果
    */

    @ApiOperation(value = "用户反馈消息",httpMethod = "POST")
    @ApiParam(name = "用户反馈消息")
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public CommonResult save(@RequestBody SuggestionsVo suggestionsvo){
        try {
            suggestionsService.save(suggestionsvo);
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new JSYException(JSYError.INTERNAL.getCode(),"保存失败");
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public CommonResult delete(@PathVariable("id") Long id){
        try {
            suggestionsService.removeById(id);
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            throw new JSYException(JSYError.INTERNAL.getCode(),"删除失败");
        }
    }

    //获取
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Suggestions get(@PathVariable("id")Long id)
    {
        return suggestionsService.getById(id);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public CommonResult json(@RequestBody SuggestionsQuery query) {
        Page<Suggestions> page = new Page<Suggestions>(query.getPage(),query.getRows());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq(StringUtils.isNotEmpty(query.getUuid()),"uuid",query.getUuid())
                .ge(query.getCreateTime()!=null,"create_time",query.getCreateTime())
                //.eq(query.getIsdeal()>=0,"isdeal",query.getIsdeal())
                .ge(query.getFinishTime()!=null,"finish_time",query.getFinishTime())
                .eq(StringUtils.isNotEmpty(query.getDealmanUuid()),"dealman_uuid",query.getDealmanUuid());

        page = suggestionsService.page(page,queryWrapper);
        return CommonResult.ok(new PageList<Suggestions>(page.getTotal(),page.getRecords()));
    }
}
