package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.service.INewUserService;
import com.jsy.domain.NewUser;
import io.swagger.annotations.ApiOperation;
import me.zhyd.oauth.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/newUser")
public class NewUserController {
    @Autowired
    public INewUserService newUserService;

    /**
     * 创建新客立减活动
     *
     * @param newUser
     * @return
     */
    @ApiOperation("创建新客立减活动")
    @PostMapping("/getNewUser/save")
    public CommonResult saveNewUser(@RequestBody NewUser newUser) {
        newUser.setUuid(UuidUtils.getUUID());
        newUser.setDeleted(1);//默认有效 未撤销
        newUser.setShopUuid(newUser.getShopUuid());
        newUserService.save(newUser);
        return CommonResult.ok();
    }

    /**
     * 撤销新客立减活动
     *
     * @return
     */
    @ApiOperation("撤销新客立减活动")
    @DeleteMapping("/deleteNewUser")
    public CommonResult deleteNewUser(@RequestParam("shopUuid") String shopUuid) {
        try {
            newUserService.update(new UpdateWrapper<NewUser>().eq("shop_uuid", shopUuid).set("deleted", 0).set("revoke_time",LocalDateTime.now()));
            return CommonResult.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResult.error(-1, "撤销失败!");
        }
    }

    /**
     * 进行中的新客立减活动
     *
     * @param
     */
    @ApiOperation("某家店进行中的新客立减活动")
    @GetMapping("/pub/getNewUser")
    public CommonResult<NewUser> getNewUser(@RequestParam("shopUuid") String shopUuid) {
        NewUser newUser = newUserService.getNewUser(shopUuid);
        return CommonResult.ok(newUser);
    }

    /**
     *最近一次的新客立减活动  1 : 进行中 2 已撤销 3 已过期 4未开始
     * @param
     */
    @ApiOperation("最近一次的新客立减活动 ")
    @GetMapping("/pub/newestNewUser")
    public CommonResult<NewUser> newestNewUser(@RequestParam("shopUuid") String shopUuid) {
        NewUser newUser = newUserService.newestNewUser(shopUuid);
        return CommonResult.ok(newUser);
    }

    /**
     * 判断该用户是否是首单（新客）
     */
    @ApiOperation("判断该用户在某家店是否是首单（新客）")
    @GetMapping("pub/isNewUser")
    public CommonResult<String> isNewUser(@RequestParam String shopUuid, @RequestParam String userUuid) {
        String newUserUuid = newUserService.isNewUser(shopUuid, userUuid);
        return CommonResult.ok(newUserUuid);
    }


}


