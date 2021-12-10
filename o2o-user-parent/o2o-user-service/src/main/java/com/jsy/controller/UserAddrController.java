package com.jsy.controller;
import com.jsy.basic.util.PageInfo;
import com.jsy.dto.UserAddrDto;
import com.jsy.param.UserAddrParam;
import com.jsy.service.IUserAddrService;
import com.jsy.query.UserAddrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

@RestController
@RequestMapping("/userAddr")
public class UserAddrController {
    @Autowired
    public IUserAddrService userAddrService;


    /**
     * 用户添加地址
     * @param userAddrParam
     * @return
     */
    @PostMapping(value="/addUserAddr")
    public CommonResult addUserAddr(@RequestBody UserAddrParam userAddrParam){

        userAddrService.addUserAddr(userAddrParam);
        return CommonResult.ok();
    }

    /**
     * 用户修改地址
     * @param userAddrParam
     * @return
     */
    @PostMapping(value="/updateUserAddr")
    public CommonResult updateUserAddr(@RequestBody UserAddrParam userAddrParam){
        userAddrService.updateUserAddr(userAddrParam);
        return CommonResult.ok();
    }

    /**
     * 用户删除地址
     * @param id
     * @return
     */
    @GetMapping(value="/deleteUserAddr")
    public CommonResult deleteUserAddr(@RequestParam("id") Long id){
        userAddrService.deleteUserAddr(id);
        return CommonResult.ok();
    }


    /**
    * 地址分页列表
    *
    * @param userAddrQuery 查询对象
    * @return  分页对象
    */
    @PostMapping(value = "/UserAddrPageList")
    public CommonResult<PageInfo<UserAddrDto>> UserAddrPageList(@RequestBody UserAddrQuery userAddrQuery)
    {
        PageInfo<UserAddrDto> pageInfo= userAddrService.UserAddrPageList(userAddrQuery);
        return CommonResult.ok(pageInfo);

    }
}
