package com.jsy.controller;
import com.jsy.parameter.GoodsParam;
import com.jsy.service.IGoodsService;
import com.jsy.domain.Goods;
import com.jsy.query.GoodsQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    public IGoodsService goodsService;

    /**
    * 添加 商品
    */
    @PostMapping(value="/saveGoods")
    public CommonResult saveGoods(@RequestBody GoodsParam goodsParam){

    }

    /**
     * 添加 服务
     */
    @PostMapping(value="/saveService")
    public CommonResult saveService(@RequestBody GoodsParam goodsParam){

    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @DeleteMapping(value="/{id}")
    public CommonResult delete(@PathVariable("id") Long id){
        try {
            goodsService.removeById(id);
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
    public Goods get(@PathVariable("id")Long id)
    {
        return goodsService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<Goods> list(){

        return goodsService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<Goods> json(@RequestBody GoodsQuery query)
    {
        Page<Goods> page = new Page<Goods>(query.getPage(),query.getRows());
        page = goodsService.page(page);
        return new PageList<Goods>(page.getTotal(),page.getRecords());
    }
}
