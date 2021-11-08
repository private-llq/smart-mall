package com.jsy.controller;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.dto.ServiceCharacteristicsDto;
import com.jsy.param.ServiceCharacteristicsParam;
import com.jsy.service.IServiceCharacteristicsService;
import com.jsy.domain.ServiceCharacteristics;
import com.jsy.query.ServiceCharacteristicsQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.baseweb.annotation.LoginIgnore;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/serviceCharacteristics")
public class ServiceCharacteristicsController {
    @Autowired
    public IServiceCharacteristicsService serviceCharacteristicsService;

    /**
    * 保存和修改公用的
    * @param characteristicsParam  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody ServiceCharacteristicsParam characteristicsParam){
        ServiceCharacteristics serviceCharacteristics = new ServiceCharacteristics();
        BeanUtils.copyProperties(characteristicsParam,serviceCharacteristics);

        try {
            if(characteristicsParam.getId()!=null){
                serviceCharacteristicsService.updateById(serviceCharacteristics);
            }else{
                System.out.println(serviceCharacteristics+"新增");
                serviceCharacteristicsService.save(serviceCharacteristics);
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
            serviceCharacteristicsService.removeById(id);
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
    public ServiceCharacteristics get(@PathVariable("id")Long id)
    {
        return serviceCharacteristicsService.getById(id);
    }


    /**
    * 返回list列表
    * @return
    */
    @LoginIgnore
    @GetMapping(value = "/list")
    public List<ServiceCharacteristicsDto> list(){
        List<ServiceCharacteristics> list = serviceCharacteristicsService.list(null);
        List<ServiceCharacteristicsDto> serviceCharacteristicsDtos = BeansCopyUtils.listCopy(list, ServiceCharacteristicsDto.class);
        return serviceCharacteristicsDtos;
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<ServiceCharacteristics> json(@RequestBody ServiceCharacteristicsQuery query)
    {
        Page<ServiceCharacteristics> page = new Page<ServiceCharacteristics>(query.getPage(),query.getRows());
        page = serviceCharacteristicsService.page(page);
        return new PageList<ServiceCharacteristics>(page.getTotal(),page.getRecords());
    }
}
