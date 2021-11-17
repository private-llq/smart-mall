package com.jsy.controller;
import com.jsy.service.IOrderGoodsService;
import com.jsy.domain.OrderGoods;
import com.jsy.query.OrderGoodsQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/orderGoods")
public class OrderGoodsController {
    @Autowired
    public IOrderGoodsService orderGoodsService;

    /**
    * 保存和修改公用的
    * @param orderGoods  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody OrderGoods orderGoods){
        try {
            if(orderGoods.getId()!=null){
                orderGoodsService.updateById(orderGoods);
            }else{
                orderGoodsService.save(orderGoods);
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
            orderGoodsService.removeById(id);
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
    public OrderGoods get(@PathVariable("id")Long id)
    {
        return orderGoodsService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<OrderGoods> list(){

        return orderGoodsService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<OrderGoods> json(@RequestBody OrderGoodsQuery query)
    {
        Page<OrderGoods> page = new Page<OrderGoods>(query.getPage(),query.getRows());
        page = orderGoodsService.page(page);
        return new PageList<OrderGoods>(page.getTotal(),page.getRecords());
    }
}
