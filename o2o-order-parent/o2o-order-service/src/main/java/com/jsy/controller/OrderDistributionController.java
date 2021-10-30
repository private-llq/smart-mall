package com.jsy.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.AjaxResult;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.OrderDistribution;
import com.jsy.query.OrderDistributionQuery;
import com.jsy.service.IOrderDistributionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/orderDistribution")
@Api(tags ="订单地址服务")
public class OrderDistributionController {

    @Autowired
    public IOrderDistributionService orderDistributionService;

    /**
    * 保存和修改公用的
    * @param orderDistribution  传递的实体
    * @return Ajaxresult转换结果
    */
    @ApiOperation(value = "订单地址新增或者修改",httpMethod = "POST",response = AjaxResult.class,notes = "当订单地址的id有的时候就是修改，没有的时候就是新增")
    @ApiParam(required = true,value = "传入订单地址对象")
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public CommonResult<Boolean> save(@RequestBody OrderDistribution orderDistribution){
        try {
            if(orderDistribution.getId()!=null){
                orderDistributionService.updateById(orderDistribution);
            }else{
                orderDistributionService.save(orderDistribution);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1,"操作失败");
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @ApiOperation(value = "订单地址表的删除",httpMethod = "DELETE",response = AjaxResult.class)
    @ApiParam(required = true,value = "传入订单地址的id")
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public CommonResult<Boolean> delete(@PathVariable("id") Long id){
        try {
            orderDistributionService.removeById(id);
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
           throw new JSYException(JSYError.INTERNAL.getCode(),"操作失败");
        }
    }

    //获取
    @ApiOperation(value = "查询订单地址",httpMethod = "GET",response = OrderDistribution.class)
    @ApiParam(required = true,value = "传入订单地址的id")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public CommonResult<OrderDistribution> get(@PathVariable("id")Long id)
    {
        return CommonResult.ok(orderDistributionService.getById(id));
    }


    /**
    * 查看所有的信息
    * @return
    */
    @ApiOperation(value = "查询所有订单地址",httpMethod = "GET",response = List.class,notes = "返回的是订单地址表的集合")
    @ApiParam(required = false,value = "不用传参")
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResult<List<OrderDistribution>> list(){

        return CommonResult.ok(orderDistributionService.list(null));
    }

    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @ApiOperation(value = "分页查询",httpMethod = "POST",response = PageList.class)
    @ApiParam(value = "可传可不参")
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public CommonResult<PageList<OrderDistribution>> json(@RequestBody OrderDistributionQuery query) {
        Page<OrderDistribution> page = new Page<OrderDistribution>(query.getPage(),query.getRows());
        page = orderDistributionService.page(page);
        return CommonResult.ok(new PageList<OrderDistribution>(page.getTotal(),page.getRecords()));
    }
}
