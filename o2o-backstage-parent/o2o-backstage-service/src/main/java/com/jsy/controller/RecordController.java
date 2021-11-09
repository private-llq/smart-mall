package com.jsy.controller;
import com.jsy.dto.RecordDto;
import com.jsy.param.RecordParam;
import com.jsy.service.IRecordService;
import com.jsy.domain.Record;
import com.jsy.query.RecordQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/record")
@Api(tags = "个人档案")
public class RecordController {
    @Autowired
    public IRecordService recordService;

    /**
    * 保存和修改公用的
    * @param record  传递的实体
    * @return Ajaxresult转换结果
    */
    @ApiOperation("保存和修改公用的")
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody RecordParam record){
        try {
            if(record.getId()!=null){
                Record record1 = new Record();
                BeanUtils.copyProperties(record,record1);
                recordService.updateById(record1);
            }else{
                Record record1 = new Record();
                BeanUtils.copyProperties(record,record1);
                recordService.save(record1);
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
            recordService.removeById(id);
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
    @GetMapping(value = "/{id}")
    public RecordDto get(@PathVariable("id")Long id)
    {
        Record record = recordService.getById(id);
        RecordDto recordDto = new RecordDto();
        BeanUtils.copyProperties(record,recordDto);
        return recordDto;
    }


    /**
    * 返回list列表
    * @return
    */
    @ApiOperation("返回list列表")
    @GetMapping(value = "/list")
    public List<RecordDto> list(){
        List<Record> list = recordService.list(null);
        List<RecordDto> recordDtos = new ArrayList<>();
        for (Record record : list) {
            RecordDto recordDto = new RecordDto();
            BeanUtils.copyProperties(record,recordDto);
            recordDtos.add(recordDto);
        }
        System.out.println(recordDtos);
        return recordDtos;
    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/pagelist")
    public PageList<Record> json(@RequestBody RecordQuery query)
    {
        Page<Record> page = new Page<Record>(query.getPage(),query.getRows());
        page = recordService.page(page);
        return new PageList<Record>(page.getTotal(),page.getRecords());
    }
}
