package com.jsy.controller;
import com.jsy.service.IOrderSetMenuService;
import com.jsy.domain.OrderSetMenu;
import com.jsy.query.OrderSetMenuQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/orderSetMenu")
public class OrderSetMenuController {
    @Autowired
    public IOrderSetMenuService orderSetMenuService;

    /**
    * 保存和修改公用的
    * @param orderSetMenu  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody OrderSetMenu orderSetMenu){
        try {
            if(orderSetMenu.getId()!=null){
                orderSetMenuService.updateById(orderSetMenu);
            }else{
                orderSetMenuService.save(orderSetMenu);
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
            orderSetMenuService.removeById(id);
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
    public OrderSetMenu get(@PathVariable("id")Long id)
    {
        return orderSetMenuService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<OrderSetMenu> list(){

        return orderSetMenuService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<OrderSetMenu> json(@RequestBody OrderSetMenuQuery query)
    {
        Page<OrderSetMenu> page = new Page<OrderSetMenu>(query.getPage(),query.getRows());
        page = orderSetMenuService.page(page);
        return new PageList<OrderSetMenu>(page.getTotal(),page.getRecords());
    }
}
