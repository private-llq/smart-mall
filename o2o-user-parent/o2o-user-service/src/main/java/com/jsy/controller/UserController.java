package com.jsy.controller;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.MyHttpUtils;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.User;
import com.jsy.query.UserQuery;
import com.jsy.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    public IUserService userService;

    @ApiOperation(value = "2.03 登录获取token", httpMethod = "POST", notes = "2.03 获取登录用户信息")
    @PostMapping("/pub/token")
    public CommonResult userLoginToken(@RequestBody User user) {
        return CommonResult.ok(userService.queryUserLogin(user));
    }

    @ApiOperation(value = "2.03 用户注册", httpMethod = "POST", notes = "2.03 用户注册")
    @PostMapping("/pub/register")
    public CommonResult register(@RequestBody User user) {
        return CommonResult.ok(userService.addUser(user));
    }

    //获取
    @GetMapping("/{uuid}")
    public CommonResult get(@PathVariable("uuid")String uuid) {
        return CommonResult.ok(userService.getByUuid(uuid));
    }

    /**
     * 传入用户ids 获取userList
     * @param ids
     * @return
     */
    @GetMapping("/list/{ids}")
    public List<User> getUserList(@PathVariable("ids")String ids)
    {
        return userService.getUserList(ids);
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public CommonResult getInfo()
    {
        return CommonResult.ok(CurrentUserHolder.getCurrentUser());
    }

    /**
    * 分页查询数据
    * @param query 查询对象
    * @return PageList 分页对象
    */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public CommonResult json(@RequestBody UserQuery query)
    {
        return CommonResult.ok(userService.queryByPage(query));
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/{uuid}")
    public CommonResult update(@PathVariable String uuid, @RequestBody User user) {
        user.setUuid(uuid);
        return CommonResult.ok(userService.updateUser(user));
    }


}
