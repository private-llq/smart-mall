package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.dto.UserRecordDto;
import com.jsy.param.UserRecordParam;
import com.jsy.service.IUserRecordService;
import com.jsy.domain.UserRecord;
import com.jsy.query.UserRecordQuery;
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
@RequestMapping("/userRecord")
@Api(tags = "用户个人档案")
public class UserRecordController {
    @Autowired
    public IUserRecordService userRecordService;

    /**
    * 保存和修改公用的
    * @param userRecordParam  传递的实体
    * @return Ajaxresult转换结果
    */
    @ApiOperation("保存和修改公用的")
    @PostMapping(value="/save")
    public CommonResult save(@RequestBody UserRecordParam userRecordParam){
        try {
            if(userRecordParam.getId()!=null){
                UserRecord userRecord = new UserRecord();
                BeanUtils.copyProperties(userRecordParam,userRecord);
                System.out.println(userRecord);
                userRecordService.updateById(userRecord);
            }else{
                UserRecord userRecord = new UserRecord();
                BeanUtils.copyProperties(userRecordParam,userRecord);
                userRecordService.save(userRecord);
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
            userRecordService.removeById(id);
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
    public CommonResult<UserRecordDto> get(@RequestParam("id")Long id)
    {
        UserRecord record = userRecordService.getById(id);
        UserRecordDto userRecordDto = new UserRecordDto();
        BeanUtils.copyProperties(record,userRecordDto);
        return CommonResult.ok(userRecordDto);
    }


    /**
    * 返回list列表
    * @return
    */
    @ApiOperation("返回list列表")
    @GetMapping(value = "/list")
    public List<UserRecordDto> list(){

        List<UserRecord> list = userRecordService.list(null);
        List<UserRecordDto> userRecordDtos = new ArrayList<>();
        for (UserRecord userRecord : list) {
            UserRecordDto userRecordDto = new UserRecordDto();
            BeanUtils.copyProperties(userRecord,userRecordDto);
            userRecordDtos.add(userRecordDto);
        }
        return userRecordDtos;
    }

    /**
     * 返回list列表
     * @return
     */
    @ApiOperation("返回list列表")
    @GetMapping(value = "/getList")
    public CommonResult< List<UserRecordDto>> getList(@RequestParam("recordId")Long recordId,@RequestParam("userId") Long userId){

        List<UserRecord> list = userRecordService.list(new QueryWrapper<UserRecord>().eq("record_id",recordId).eq("user_id",userId));
        List<UserRecordDto> userRecordDtos = new ArrayList<>();
        for (UserRecord userRecord : list) {
            UserRecordDto userRecordDto = new UserRecordDto();
            BeanUtils.copyProperties(userRecord,userRecordDto);
            userRecordDtos.add(userRecordDto);
        }
        if (userRecordDtos!=null){
            return CommonResult.ok(userRecordDtos);
        }else {
            return  CommonResult.ok(null);
        }

    }


    /**
    * 分页查询数据
    *
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @ApiOperation("分页查询数据")
    @PostMapping(value = "/pagelist")
    public PageList<UserRecord> json(@RequestBody UserRecordQuery query)
    {
        Page<UserRecord> page = new Page<UserRecord>(query.getPage(),query.getRows());
        page = userRecordService.page(page);
        return new PageList<UserRecord>(page.getTotal(),page.getRecords());
    }
}
