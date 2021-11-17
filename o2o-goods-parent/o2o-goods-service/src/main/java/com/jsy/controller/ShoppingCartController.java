package com.jsy.controller;
import com.jsy.service.IShoppingCartService;
import com.jsy.domain.ShoppingCart;
import com.jsy.query.ShoppingCartQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    public IShoppingCartService shoppingCartService;

    /**
    * 保存和修改公用的
    * @param shoppingCart  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody ShoppingCart shoppingCart){
        try {
            if(shoppingCart.getId()!=null){
                shoppingCartService.updateById(shoppingCart);
            }else{
                shoppingCartService.save(shoppingCart);
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
            shoppingCartService.removeById(id);
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
    public ShoppingCart get(@PathVariable("id")Long id)
    {
        return shoppingCartService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<ShoppingCart> list(){

        return shoppingCartService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<ShoppingCart> json(@RequestBody ShoppingCartQuery query)
    {
        Page<ShoppingCart> page = new Page<ShoppingCart>(query.getPage(),query.getRows());
        page = shoppingCartService.page(page);
        return new PageList<ShoppingCart>(page.getTotal(),page.getRecords());
    }
}
