package com.jsy.controller;
import com.jsy.service.IOrderServiceService;
import com.jsy.domain.OrderService;
import com.jsy.query.OrderServiceQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/orderService")
public class OrderServiceController {
    @Autowired
    public IOrderServiceService orderServiceService;

    /**
    * 保存和修改公用的
    * @param orderService  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody OrderService orderService){
        try {
            if(orderService.getId()!=null){
                orderServiceService.updateById(orderService);
            }else{
                orderServiceService.save(orderService);
            }
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1,"操作失败！");
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @DeleteMapping(value="/{id}")
    public CommonResult delete(@PathVariable("id") Long id){
        try {
            orderServiceService.removeById(id);
            return CommonResult.ok();
        } catch (Exception e) {
        e.printStackTrace();
            return  CommonResult.error(-1,"删除失败！");
        }
    }

    /**
    * 根据id查询一条
    * @param id
    */
    @GetMapping(value = "/{id}")
    public OrderService get(@PathVariable("id")Long id)
    {
        return orderServiceService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<OrderService> list(){

        return orderServiceService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<OrderService> json(@RequestBody OrderServiceQuery query)
    {
        Page<OrderService> page = new Page<OrderService>(query.getPage(),query.getRows());
        page = orderServiceService.page(page);
        return new PageList<OrderService>(page.getTotal(),page.getRecords());
    }
}
