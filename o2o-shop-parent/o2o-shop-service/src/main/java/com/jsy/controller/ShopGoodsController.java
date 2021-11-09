package com.jsy.controller;
import com.jsy.service.IShopGoodsService;
import com.jsy.domain.ShopGoods;
import com.jsy.query.ShopGoodsQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/shopGoods")
public class ShopGoodsController {
    @Autowired
    public IShopGoodsService shopGoodsService;

    /**
    * 保存和修改公用的
    * @param shopGoods  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody ShopGoods shopGoods){
        try {
            if(shopGoods.getId()!=null){
                shopGoodsService.updateById(shopGoods);
            }else{
                shopGoodsService.save(shopGoods);
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
            shopGoodsService.removeById(id);
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
    public ShopGoods get(@PathVariable("id")Long id)
    {
        return shopGoodsService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<ShopGoods> list(){

        return shopGoodsService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<ShopGoods> json(@RequestBody ShopGoodsQuery query)
    {
        Page<ShopGoods> page = new Page<ShopGoods>(query.getPage(),query.getRows());
        page = shopGoodsService.page(page);
        return new PageList<ShopGoods>(page.getTotal(),page.getRecords());
    }
}
