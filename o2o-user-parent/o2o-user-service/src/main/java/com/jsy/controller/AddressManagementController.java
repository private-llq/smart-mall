package com.jsy.controller;
import com.jsy.service.IAddressManagementService;
import com.jsy.domain.AddressManagement;
import com.jsy.query.AddressManagementQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/addressManagement")
public class AddressManagementController {
    @Autowired
    public IAddressManagementService addressManagementService;

    /**
    * 保存和修改公用的
    * @param addressManagement  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody AddressManagement addressManagement){
        try {
            if(addressManagement.getId()!=null){
                addressManagementService.updateById(addressManagement);
            }else{
                addressManagementService.save(addressManagement);
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
            addressManagementService.removeById(id);
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
    public AddressManagement get(@PathVariable("id")Long id)
    {
        return addressManagementService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @GetMapping(value = "/list")
    public List<AddressManagement> list(){

        return addressManagementService.list(null);
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<AddressManagement> json(@RequestBody AddressManagementQuery query)
    {
        Page<AddressManagement> page = new Page<AddressManagement>(query.getPage(),query.getRows());
        page = addressManagementService.page(page);
        return new PageList<AddressManagement>(page.getTotal(),page.getRecords());
    }
}
