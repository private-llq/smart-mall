package com.jsy.controller;
import com.jsy.service.ISetMenuGoodsService;
import com.jsy.domain.SetMenuGoods;
import com.jsy.query.SetMenuGoodsQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/setMenuGoods")
public class SetMenuGoodsController {
    @Autowired
    public ISetMenuGoodsService setMenuGoodsService;

    /**
    * 保存和修改公用的
    * @param setMenuGoods  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody SetMenuGoods setMenuGoods){
        try {
            if(setMenuGoods.getId()!=null){
                setMenuGoodsService.updateById(setMenuGoods);
            }else{
                setMenuGoodsService.save(setMenuGoods);
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
            setMenuGoodsService.removeById(id);
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
    public SetMenuGoods get(@PathVariable("id")Long id)
    {
        return setMenuGoodsService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<SetMenuGoods> list(){

        return setMenuGoodsService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<SetMenuGoods> json(@RequestBody SetMenuGoodsQuery query)
    {
        Page<SetMenuGoods> page = new Page<SetMenuGoods>(query.getPage(),query.getRows());
        page = setMenuGoodsService.page(page);
        return new PageList<SetMenuGoods>(page.getTotal(),page.getRecords());
    }
}
