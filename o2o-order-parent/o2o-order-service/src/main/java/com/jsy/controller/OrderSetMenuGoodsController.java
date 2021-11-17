package com.jsy.controller;
import com.jsy.service.IOrderSetMenuGoodsService;
import com.jsy.domain.OrderSetMenuGoods;
import com.jsy.query.OrderSetMenuGoodsQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/orderSetMenuGoods")
public class OrderSetMenuGoodsController {
    @Autowired
    public IOrderSetMenuGoodsService orderSetMenuGoodsService;

    /**
    * 保存和修改公用的
    * @param orderSetMenuGoods  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody OrderSetMenuGoods orderSetMenuGoods){
        try {
            if(orderSetMenuGoods.getId()!=null){
                orderSetMenuGoodsService.updateById(orderSetMenuGoods);
            }else{
                orderSetMenuGoodsService.save(orderSetMenuGoods);
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
            orderSetMenuGoodsService.removeById(id);
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
    public OrderSetMenuGoods get(@PathVariable("id")Long id)
    {
        return orderSetMenuGoodsService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<OrderSetMenuGoods> list(){

        return orderSetMenuGoodsService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<OrderSetMenuGoods> json(@RequestBody OrderSetMenuGoodsQuery query)
    {
        Page<OrderSetMenuGoods> page = new Page<OrderSetMenuGoods>(query.getPage(),query.getRows());
        page = orderSetMenuGoodsService.page(page);
        return new PageList<OrderSetMenuGoods>(page.getTotal(),page.getRecords());
    }
}
