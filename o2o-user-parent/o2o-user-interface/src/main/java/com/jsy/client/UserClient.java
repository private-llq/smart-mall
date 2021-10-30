package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "SERVICE-USER",configuration = FeignConfiguration.class)
public interface UserClient {

    @PostMapping("user/updateUser")
    CommonResult<Boolean> update(@RequestBody User user);

    @GetMapping("user/list/{ids}")
    List<User> getUserList(@PathVariable("ids") String ids);



}
