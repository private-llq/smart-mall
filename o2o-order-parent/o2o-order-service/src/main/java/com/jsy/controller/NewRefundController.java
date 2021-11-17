package com.jsy.controller;
import com.jsy.service.INewRefundService;
import com.jsy.domain.NewRefund;
import com.jsy.query.NewRefundQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/newRefund")
public class NewRefundController {
    @Autowired
    public INewRefundService newRefundService;

    /**
    * 保存和修改公用的
    * @param newRefund  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody NewRefund newRefund){
        try {
            if(newRefund.getId()!=null){
                newRefundService.updateById(newRefund);
            }else{
                newRefundService.save(newRefund);
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
            newRefundService.removeById(id);
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
    public NewRefund get(@PathVariable("id")Long id)
    {
        return newRefundService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<NewRefund> list(){

        return newRefundService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<NewRefund> json(@RequestBody NewRefundQuery query)
    {
        Page<NewRefund> page = new Page<NewRefund>(query.getPage(),query.getRows());
        page = newRefundService.page(page);
        return new PageList<NewRefund>(page.getTotal(),page.getRecords());
    }
}
