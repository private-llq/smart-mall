package com.jsy.client;


import com.jsy.FeignConfiguration;
import com.jsy.basic.util.AjaxResult;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.OrderClientImpl;
import com.jsy.domain.ShopEvaluation;
import com.jsy.query.ShopEvaluationQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "SERVICE-ORDER",fallback = OrderClientImpl.class,configuration = FeignConfiguration.class)
public interface ShopEvaluationClient {

    /**
     * 保存和修改公用的
     * @param shopEvaluation  传递的实体
     * @return Ajaxresult转换结果
     */
    @ApiOperation(value = "店铺评论新增",httpMethod = "POST",response = AjaxResult.class,notes = "当店铺评论的id有的时候就是修改，没有的时候就是新增")
    @ApiParam(required = true,value = "传入店铺评论对象")
    @RequestMapping(value="/saveEvaluation",method= RequestMethod.POST)
    public CommonResult saveEvaluation(@RequestBody ShopEvaluation shopEvaluation);

    /**
     * 删除店铺的评论信息
     * @param id
     * @return
     */
    @ApiOperation(value = "店铺评论的删除",httpMethod = "DELETE",response = AjaxResult.class)
    @ApiParam(required = true,value = "根据id删除")
    @RequestMapping(value="/deleteEvaluation/{id}",method=RequestMethod.DELETE)
    public CommonResult<Boolean> deleteEvaluation(@PathVariable("id") Long id);

    /**
     * 不建议使用，管理员可根据店家id删除所有评论信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/deleteBySid/{id}",method = RequestMethod.DELETE)
    public CommonResult deleteBySid(@PathVariable("id") Long id);

    /**
     * 分页查询数据
     *
     * @param query 查询对象
     * @return PageList 分页对象
     */
    @ApiOperation(value = "分页查询",httpMethod = "POST",response = CommonResult.class)
    @ApiParam(value = "分页查询")
    @RequestMapping(value = "/page",method = RequestMethod.POST)
    public CommonResult pageEvaluation(@RequestBody ShopEvaluationQuery query);

    /**
     * 查询用户已评价信息
     * @param id
     * @return
     */
    @ApiOperation(value = "查询用户已评价信息",httpMethod = "GET",response = CommonResult.class)
    @ApiParam(value = "查询用户已评价信息")
    @RequestMapping(value = "/getEvaluations/{id}",method = RequestMethod.GET)
    public CommonResult getEvaluations(@RequestParam("id") Long id);
}
