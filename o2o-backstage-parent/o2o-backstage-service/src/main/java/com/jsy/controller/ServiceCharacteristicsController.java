package com.jsy.controller;
import com.alibaba.druid.sql.visitor.functions.If;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.dto.ServiceCharacteristicsDto;
import com.jsy.param.ServiceCharacteristicsParam;
import com.jsy.service.IServiceCharacteristicsService;
import com.jsy.domain.ServiceCharacteristics;
import com.jsy.query.ServiceCharacteristicsQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhsj.baseweb.annotation.LoginIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/serviceCharacteristics")
@Api(tags = "服务特点")
public class ServiceCharacteristicsController {
    @Autowired
    public IServiceCharacteristicsService serviceCharacteristicsService;

    /**
    * 保存和修改公用的
    * @param characteristicsParam  传递的实体
    * @return Ajaxresult转换结果
    */
    @PostMapping(value="/save")
    @ApiOperation("保存和修改公用的")
    public CommonResult save(@RequestBody ServiceCharacteristicsParam characteristicsParam){
        try {
            if(characteristicsParam.getId()!=null){
                ServiceCharacteristics serviceCharacteristics = new ServiceCharacteristics();
                BeanUtils.copyProperties(characteristicsParam,serviceCharacteristics);
                serviceCharacteristicsService.updateById(serviceCharacteristics);
            }else{
                ServiceCharacteristics serviceCharacteristics = new ServiceCharacteristics();
                BeanUtils.copyProperties(characteristicsParam,serviceCharacteristics);
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
    @ApiOperation("删除对象信息")
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
    @ApiOperation("根据id查询一条")
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public CommonResult<ServiceCharacteristics> get(@PathVariable("id")Long id)
    {
        ServiceCharacteristics service = serviceCharacteristicsService.getById(id);
        if (service!=null){
            return CommonResult.ok(service);
        }else {
            return new  CommonResult(-1,"失败",null);
        }
    }


    /**
    * 返回list列表
    * @return
    */
    @ApiOperation("返回list列表")
    @LoginIgnore
    @GetMapping(value = "/list")
    public CommonResult<List<ServiceCharacteristicsDto>> list(){
        List<ServiceCharacteristics> list = serviceCharacteristicsService.list(null);
        List<ServiceCharacteristicsDto> serviceCharacteristicsDtos = BeansCopyUtils.listCopy(list, ServiceCharacteristicsDto.class);
        if(serviceCharacteristicsDtos!=null){
            return CommonResult.ok(serviceCharacteristicsDtos);
        }else {
            return new  CommonResult(-1,"失败",null);
        }
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
