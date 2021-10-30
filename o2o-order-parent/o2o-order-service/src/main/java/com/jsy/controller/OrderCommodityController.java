package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.AjaxResult;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.OrderCommodity;
import com.jsy.query.OrderCommodityQuery;
import com.jsy.service.IOrderCommodityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author yu
 */
@RestController
@RequestMapping("/orderCommodity")
@Api(tags = "订单商品表")
public class OrderCommodityController {


    @Autowired
    public IOrderCommodityService orderCommodityService;

    public static final Logger log = LoggerFactory.getLogger(OrderCommodityController.class);
    /**
    * 保存和修改公用的
    * @param orderCommodity  传递的实体
    * @return Ajaxresult转换结果
    */
    @ApiOperation(value = "订单商品新增或者修改",httpMethod = "POST",response = AjaxResult.class,notes = "当订单商品的id有的时候就是修改，没有的时候就是新增")
    @ApiParam(required = true,value = "传入订单商品对象")
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public CommonResult<Boolean> save(@RequestBody OrderCommodity orderCommodity){
        try {
            if(orderCommodity.getId()!=null){
                orderCommodityService.updateById(orderCommodity);
            }else{
                orderCommodityService.save(orderCommodity);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
            return CommonResult.error(-1,"操作失败");
        }
    }

    /**
    * 删除对象信息
    * @param orderUuid
    * @return
    */
    @ApiOperation(value = "订单商品表的删除",httpMethod = "DELETE",response = AjaxResult.class)
    @ApiParam(required = true,value = "传入订单商品的id")
    @RequestMapping(value="/delete",method=RequestMethod.DELETE)
    public CommonResult delete(@RequestParam("orderUuid") String orderUuid){
        int i = orderCommodityService.delete(orderUuid);
        if (i==0){
            return CommonResult.error(-1,"操作失败");
        }
        return CommonResult.ok();
    }

    //获取
    @ApiOperation(value = "查询订单商品",httpMethod = "GET",response = OrderCommodity.class)
    @ApiParam(required = true,value = "传入订单商品的id")
    @RequestMapping(value = "/get",method = RequestMethod.GET)
    public CommonResult get(@RequestParam("orderUuid")String orderUuid) {
        List<OrderCommodity> list = null;

        try {
            list = orderCommodityService.getByOid(orderUuid);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return CommonResult.ok(list);
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
    public CommonResult<PageList<OrderCommodity>>json(@RequestBody OrderCommodityQuery query) {
        Page<OrderCommodity> page = new Page<OrderCommodity>(query.getPage(),query.getRows());
        page = orderCommodityService.page(page);
        return CommonResult.ok(new PageList<OrderCommodity>(page.getTotal(),page.getRecords()));
    }

    /**
     * 待做
     * @param query
     * @return
     */
    @ApiOperation(value = "统计对应商品月销量")
    @RequestMapping(value = "/list",method = RequestMethod.POST)
    public CommonResult monthSales(@RequestBody OrderCommodityQuery query){
        QueryWrapper queryWrapper  = new QueryWrapper();
         queryWrapper.ge(query.getCreateTime1()!=null,"create_time",query.getCreateTime1())
                .le(query.getCreateTime2()!=null,"create_time",query.getCreateTime2())
                 .eq(StringUtils.isNotBlank(query.getGoodsUuid()),"goods_uuid",query.getGoodsUuid());
         int i = orderCommodityService.monthSales(queryWrapper);
        return CommonResult.ok(i);
    }

}
