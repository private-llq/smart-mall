package com.jsy.controller;


import com.jsy.basic.util.RedisStateCache;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.basic.util.vo.UserEntity;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("解析token")
@RequestMapping("/forUser")
public class forUserController {
    @Autowired
    private RedisStateCache redisStateCache;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public CommonResult dealToken(){
        UserDto currentUser = CurrentUserHolder.getCurrentUser();
        return CommonResult.ok(currentUser);
    }

    @RequestMapping(value = "/user22",method = RequestMethod.GET)
    public CommonResult dealTokenSheQu(){
        UserEntity currentUser = CurrentUserHolder.getUserEntity();
        return CommonResult.ok(currentUser);
    }
}
