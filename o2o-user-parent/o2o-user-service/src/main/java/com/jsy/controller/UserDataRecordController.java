package com.jsy.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.UserDataRecord;
import com.jsy.dto.MatchTheUserDto;
import com.jsy.query.UserDataRecordQuery;
import com.jsy.service.IUserDataRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userDataRecord")
public class UserDataRecordController {
    @Autowired
    public IUserDataRecordService userDataRecordService;

    /**
    * 新增
    * @param userDataRecord  实体类对象
    * @return CommonResult 公共返回类
    */
    @PostMapping("/saveUserDataRecord")
    public CommonResult saveUserDataRecord(@RequestBody UserDataRecord userDataRecord){
        try {
            userDataRecordService.saveUserDataRecord(userDataRecord);
            return CommonResult.ok();
        } catch (Exception ex) {
            return CommonResult.error(-1,"操作失败！");
        }

    }

    /**
     * 修改
     * @param userDataRecord  实体类对象
     * @return CommonResult 公共返回类
     */
    @PostMapping("/updateUserDataRecord")
    public CommonResult updateUserDataRecord(@RequestBody UserDataRecord userDataRecord){
        try {
             userDataRecordService.updateById(userDataRecord);
            return CommonResult.ok();
        } catch (Exception ex) {
            return CommonResult.error(-1,"操作失败！");
        }
    }

    /**
    * 删除
    * @param id
    * @return CommonResult 公共返回类
    */
    @DeleteMapping("/deleteUserDataRecord")
    public CommonResult deleteUserDataRecord(@RequestParam("id") Long id){
        try {
            userDataRecordService.removeById(id);
            return CommonResult.ok();
        } catch (Exception ex) {
            return CommonResult.error(-1,"操作失败！");
        }
    }

    /**
    * 查询
    * @param imId
    */
    @GetMapping("/getUserDataRecord")
    public CommonResult<List<UserDataRecord>> getUserDataRecord(@RequestParam("imId")String imId)
    {
        List<UserDataRecord> getUserDataRecord=userDataRecordService.getUserDataRecord(imId);
        return CommonResult.ok(getUserDataRecord);
    }

    /**
     * 查询
     * @param treeId
     */
    @GetMapping("/getUserDataRecordTreeId")
    public CommonResult<List<UserDataRecord>> getUserDataRecordTreeId(@RequestParam("treeId")Long treeId)
    {
        List<UserDataRecord> getUserDataRecord=userDataRecordService.getUserDataRecordTreeId(treeId);
        return CommonResult.ok(getUserDataRecord);
    }



    /**
    * list列表
    * @return
    */
    @GetMapping("/listUserDataRecord")
    public CommonResult<List<UserDataRecord>> listUserDataRecord(@RequestParam("type") Integer type){
        List<UserDataRecord> list=userDataRecordService.listUserDataRecord(type);
        return CommonResult.ok(list);
    }


    /**
    * 分页查询数据
    * @param query 查询对象
    * @return PageInfo 分页对象
    */
    @PostMapping("/pageUserDataRecord")
    public CommonResult<PageInfo<UserDataRecord>> pageUserDataRecord(@RequestBody UserDataRecordQuery query)
    {
        Page<UserDataRecord> page = new Page(query.getPage(),query.getRows());
        Page<UserDataRecord> selectPage = userDataRecordService.page(page);
        PageInfo<UserDataRecord> pageInfo=new PageInfo();
        pageInfo.setRecords(selectPage.getRecords());pageInfo.setTotal(selectPage.getTotal());
        pageInfo.setSize(selectPage.getSize());pageInfo.setCurrent(selectPage.getCurrent());
        return CommonResult.ok(pageInfo);
    }

    /**
     * 根据商家的模板来匹配用户
     */
    @PostMapping("/matchTheUser")
    public CommonResult<PageInfo<MatchTheUserDto>> matchTheUser(@RequestBody UserDataRecordQuery query)
    {
        PageInfo<MatchTheUserDto> pageInfo= userDataRecordService.matchTheUser(query);

        return CommonResult.ok(pageInfo);
    }
}
