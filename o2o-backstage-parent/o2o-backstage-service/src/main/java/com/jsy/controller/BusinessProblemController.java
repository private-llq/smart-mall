package com.jsy.controller;
import com.jsy.service.IBusinessProblemService;
import com.jsy.domain.BusinessProblem;
import com.jsy.query.BusinessProblemQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/businessProblem")
public class BusinessProblemController {
    @Autowired
    public IBusinessProblemService businessProblemService;

    /**
    * 保存和修改公用的
    * @param businessProblem  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody BusinessProblem businessProblem){
        try {
            if(businessProblem.getUuid()!=null){
                businessProblemService.updateById(businessProblem);
            }else{
                businessProblemService.save(businessProblem);
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
            businessProblemService.removeById(id);
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
    public BusinessProblem get(@PathVariable("id")Long id)
    {
        return businessProblemService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<BusinessProblem> list(){

        return businessProblemService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<BusinessProblem> json(@RequestBody BusinessProblemQuery query)
    {
        Page<BusinessProblem> page = new Page<BusinessProblem>(query.getPage(),query.getRows());
        page = businessProblemService.page(page);
        return new PageList<BusinessProblem>(page.getTotal(),page.getRecords());
    }
}
