package com.jsy.controller;
import com.jsy.query.BusinessFeedtypeQuery;
import com.jsy.service.IBusinessFeedtypeService;
import com.jsy.domain.BusinessFeedtype;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/userFeedtype")
public class BusinessFeedtypeController {
    @Autowired
    public IBusinessFeedtypeService userFeedtypeService;

    /**
     * 返回list列表
     * @return
     */
    @GetMapping(value = "/list")
    public CommonResult<List<BusinessFeedtype>> list(){
        List<BusinessFeedtype> businessFeedtypes = userFeedtypeService.list(null);
        return new CommonResult(200,"操作成功", businessFeedtypes);
    }


    /**
    * 保存和修改公用的
    * @param businessFeedtype  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody BusinessFeedtype businessFeedtype){
        try {
            if(businessFeedtype.getId()!=null){
                userFeedtypeService.updateById(businessFeedtype);
            }else{
                userFeedtypeService.save(businessFeedtype);
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
            userFeedtypeService.removeById(id);
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
    public BusinessFeedtype get(@PathVariable("id")Long id)
    {
        return userFeedtypeService.getById(id);
    }





    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<BusinessFeedtype> json(@RequestBody BusinessFeedtypeQuery query)
    {
        Page<BusinessFeedtype> page = new Page<BusinessFeedtype>(query.getPage(),query.getRows());
        page = userFeedtypeService.page(page);
        return new PageList<BusinessFeedtype>(page.getTotal(),page.getRecords());
    }
}
