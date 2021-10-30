package com.jsy.controller;
import com.jsy.basic.util.PageList;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.dto.UserFollowDTO;
import com.jsy.query.UserFollowQuery;
import com.jsy.service.IUserFollowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/userFollow")
@Api(tags ="用户关注模块")
public class UserFollowController {

    @Autowired
    private IUserFollowService userFollowService;

    @ApiOperation(value = "用户关注店铺", httpMethod = "POST", notes = "2.03 用户关注")
    @PostMapping("/follow/{shopUuid}")
    public CommonResult follow(@PathVariable("shopUuid") String shopUuid) {
        userFollowService.follow(shopUuid);
        return CommonResult.ok();
    }

    @ApiOperation(value = "用户分页查询关注店铺列表", httpMethod = "POST", notes = "2.03 用户关注")
    @PostMapping("/followList")
    public CommonResult followList(@RequestBody UserFollowQuery userFollowQuery){
        PageList<UserFollowDTO> list = userFollowService.followList(userFollowQuery);
        return CommonResult.ok(list);
    }

    @ApiOperation(value = "取消关注", httpMethod = "DELETE", notes = "2.03 用户取消关注")
    @DeleteMapping("/outFollow/{shopUuid}")
    public CommonResult outFollow(@PathVariable("shopUuid") String shopUuid){
        try {
            userFollowService.outFollow(shopUuid);
        } catch (Exception e) {
            e.printStackTrace();
            CommonResult.error(-1,"操作失败");
        }
        return CommonResult.ok();
    }


    @ApiOperation(value = "进店查寻关注状态 1：已关注，0：未关注", httpMethod = "GET", notes = "2.03 用户关注状态")
    @GetMapping("/followStatus/{shopUuid}")
    public CommonResult followStatus(@PathVariable("shopUuid") String shopUuid){
        Integer status = userFollowService.FollowStatus(shopUuid);
        return CommonResult.ok(status);
    }



}
