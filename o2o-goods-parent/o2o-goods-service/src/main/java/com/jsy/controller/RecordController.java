package com.jsy.controller;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.AjaxResult;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.Record;
import com.jsy.dto.RecordDTO;
import com.jsy.query.RecordQuery;
import com.jsy.service.IRecordService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Api(tags = "商品抢购记录")
@RestController
@RequestMapping("/record")
public class RecordController {
    @Autowired
    public IRecordService recordService;
    /**
    * 保存和修改公用的
    * @param record  传递的实体
    * @return Ajaxresult转换结果
    */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public AjaxResult save(@RequestBody Record record){
        try {
            if(record.getId()!=null){
                recordService.updateById(record);
            }else{
                recordService.save(record);
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
            recordService.removeById(id);
            return AjaxResult.me();
        } catch (Exception e) {
        e.printStackTrace();
            return AjaxResult.me().setMessage("删除对象失败！"+e.getMessage());
        }
    }
    //获取
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Record get(@PathVariable("id")Long id)
    {
        return recordService.getById(id);
    }
    /**
     * 查看所有抢购到商品的用户信息
     * @return
     */
    @RequestMapping(value = "/listUserRecord",method = RequestMethod.GET)
    public CommonResult listUserRecord(String shopUuid, String goodsUuid){
        List<RecordDTO> recordDTOS = recordService.listUserRecord(shopUuid, goodsUuid);
        return CommonResult.ok(recordDTOS);
    }
    /**
    * 分页查询数据
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public PageList<Record> json(@RequestBody RecordQuery query)
    {
        Page<Record> page = new Page<Record>(query.getPage(),query.getRows());
        page = recordService.page(page);
        return new PageList<Record>(page.getTotal(),page.getRecords());
    }
}
