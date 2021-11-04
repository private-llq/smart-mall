package com.jsy.controller;
import com.jsy.service.IIndustryCategoryService;
import com.jsy.domain.IndustryCategory;
import com.jsy.query.IndustryCategoryQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/industryCategory")
public class IndustryCategoryController {
    @Autowired
    public IIndustryCategoryService industryCategoryService;

    /**
    * 保存和修改公用的
    * @param industryCategory  传递的实体
    * @return Ajaxresult转换结果
    */
    @ApiOperation(value = "新增或删除",httpMethod = "Delete",response = CommonResult.class)
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody IndustryCategory industryCategory){
        try {
            if(industryCategory.getId()!=null){
                industryCategoryService.updateById(industryCategory);
            }else{
                industryCategoryService.save(industryCategory);
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
    @ApiOperation(value = "删除对象信息",httpMethod = "Delete",response = CommonResult.class)
    @DeleteMapping(value="/{id}")
    public CommonResult delete(@PathVariable("id") Long id){
        try {
            industryCategoryService.removeById(id);
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
//    @ApiOperation("根据id查询")
    @GetMapping(value = "/{id}")
    public IndustryCategory get(@PathVariable("id")Long id)
    {
        return industryCategoryService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @ApiOperation(value = "查询分类结果",httpMethod = "GET",response = CommonResult.class)
    @GetMapping(value = "/list")
    public List<IndustryCategory> list(){

        return industryCategoryService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
//    @ApiOperation(value = "获取分页数据",httpMethod = "POST",response = CommonResult.class)
    @PostMapping(value = "/pagelist")
    public PageList<IndustryCategory> json(@RequestBody IndustryCategoryQuery query)
    {
        Page<IndustryCategory> page = new Page<IndustryCategory>(query.getPage(),query.getRows());
        page = industryCategoryService.page(page);
        return new PageList<IndustryCategory>(page.getTotal(),page.getRecords());
    }
}
