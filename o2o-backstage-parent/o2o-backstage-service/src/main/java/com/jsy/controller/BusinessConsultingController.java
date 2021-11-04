package com.jsy.controller;
import com.jsy.service.IBusinessConsultingService;
import com.jsy.domain.BusinessConsulting;
import com.jsy.query.BusinessConsultingQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/businessConsulting")
public class BusinessConsultingController {
    @Autowired
    public IBusinessConsultingService businessConsultingService;

    /**
    * 保存和修改公用的
    * @param businessConsulting  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody BusinessConsulting businessConsulting){
        try {
            if(businessConsulting.getUuid()!=null){
                businessConsultingService.updateById(businessConsulting);
            }else{
                businessConsultingService.save(businessConsulting);
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
            businessConsultingService.removeById(id);
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
    public BusinessConsulting get(@PathVariable("id")Long id)
    {
        return businessConsultingService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<BusinessConsulting> list(){

        return businessConsultingService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<BusinessConsulting> json(@RequestBody BusinessConsultingQuery query)
    {
        Page<BusinessConsulting> page = new Page<BusinessConsulting>(query.getPage(),query.getRows());
        page = businessConsultingService.page(page);
        return new PageList<BusinessConsulting>(page.getTotal(),page.getRecords());
    }
}
