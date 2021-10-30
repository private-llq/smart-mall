package com.jsy.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.AjaxResult;
import com.jsy.basic.util.PageList;
import com.jsy.domain.ShopEvaluation;
import com.jsy.query.ShopEvaluationQuery;
import com.jsy.service.IShopEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/shopEvaluation")
public class ShopEvaluationController {
    @Autowired
    public IShopEvaluationService shopEvaluationService;

    /**
    * 保存和修改公用的
    * @param shopEvaluation  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody ShopEvaluation shopEvaluation){
        try {
            if(shopEvaluation.getId()!=null){
                shopEvaluationService.updateById(shopEvaluation);
            }else{
                shopEvaluationService.save(shopEvaluation);
            }
            return AjaxResult.me();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setMessage("保存对象失败！"+e.getMessage());
        }
    }

    /**
    * 删除对象信息
    * @param id
    * @return
    */
    @RequestMapping(value="/{id}",method=RequestMethod.DELETE)
    public AjaxResult delete(@PathVariable("id") Long id){
        try {
            shopEvaluationService.removeById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }

    //获取
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public ShopEvaluation get(@PathVariable("id")Long id)
    {
        return shopEvaluationService.getById(id);
    }

    /**
    * 查看所有的信息
    * @return
    */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public List<ShopEvaluation> list(){

        return shopEvaluationService.list(null);
    }

    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public PageList<ShopEvaluation> json(@RequestBody ShopEvaluationQuery query)
    {
        Page<ShopEvaluation> page = new Page<ShopEvaluation>(query.getPage(),query.getRows());
        page = shopEvaluationService.page(page);
        return new PageList<ShopEvaluation>(page.getTotal(),page.getRecords());
    }
}
