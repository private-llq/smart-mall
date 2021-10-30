package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.utils.ValidatorUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.domain.ShopOwner;
import com.jsy.query.ShopOwnerQuery;
import com.jsy.service.IShopOwnerService;
import com.jsy.vo.PassVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shopOwner")
public class ShopOwnerController {
    @Autowired
    public IShopOwnerService shopOwnerService;

    @ApiOperation(value = "2.03 登录获取token", httpMethod = "POST", notes = "2.03 获取登录用户信息")
    @PostMapping("/pub/token")
    public CommonResult shopOwnerLoginToken(@RequestBody ShopOwner shopOwner) {
        return CommonResult.ok(shopOwnerService.queryShopOwnerLogin(shopOwner));
    }

/*    @ApiOperation(value = "2.03 用户注册", httpMethod = "POST", notes = "2.03 用户注册")
    @PostMapping("/pub/register")
    public CommonResult register(@RequestBody ShopOwner shopOwner) {
        return CommonResult.ok(shopOwnerService.addShopOwner(shopOwner));
    }*/

    //获取
    @GetMapping("/{uuid}")
    public CommonResult get(@PathVariable("uuid")String uuid) {
        return CommonResult.ok(shopOwnerService.getByUuid(uuid));
    }

    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public CommonResult getInfo()
    {
        return CommonResult.ok(CurrentUserHolder.getCurrentUser());
    }

    /**
     * 分页查询数据
     *
     * @param query 查询对象
     * @return PageList 分页对象
     */
    @RequestMapping(value = "/pagelist",method = RequestMethod.POST)
    public CommonResult json(@RequestBody ShopOwnerQuery query)
    {
        return CommonResult.ok(shopOwnerService.queryByPage(query));
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/{uuid}")
    public CommonResult update(@PathVariable String uuid, @RequestBody ShopOwner shopOwner) {
        shopOwner.setUuid(uuid);
        return CommonResult.ok(shopOwnerService.updateShopOwner(shopOwner));
    }


    @ApiOperation("根据relationUuid获取店铺拥有者信息")
    @GetMapping("/getByRelationUid/{uuid}")
    public CommonResult getByRelationUid(@PathVariable("uuid") String uuid){
        ShopOwner shopOwner = shopOwnerService.getByRelationUid(uuid);
        return CommonResult.ok(shopOwner);
        //return new CommonResult(200,"查询成功",shopOwner);
    }

    @ApiOperation("根据登入店铺用户获取店铺拥有者信息")
    @GetMapping("/getByUuid")
    public CommonResult getByUuid(){
        UserDto user = CurrentUserHolder.getCurrentUser();
        ShopOwner shopOwner = shopOwnerService.getByUuid(user.getUuid());
        return CommonResult.ok(shopOwner);
    }

    /**
     * 发送短信到手机号
     * 先存验证码到数据库，手机然后再收到验证码
     * @param phoneNumber
     * @return
     */
    @ApiOperation("向手机发送验证码")
    @GetMapping("/pub/sendSmsCode")
    public  CommonResult sendSmsCode(@RequestParam String phoneNumber) {
        boolean b = shopOwnerService.sendSmsPassword(phoneNumber);
        return CommonResult.ok(b);
    }

    /**
     * 验证码登录
     */
    @ApiOperation("验证码登录")
    @GetMapping("/pub/loginSmsCode")
    public CommonResult<String> loginSmsCode(@RequestParam String phoneNumber, @RequestParam Integer code){
        ValidatorUtils.validateEntity(phoneNumber);
        String s = shopOwnerService.loginSms(phoneNumber, code);
        return CommonResult.ok(s);
    }

    /**
     * 本地手机一键登录
     */
    @ApiOperation("一键登录")
    @GetMapping("/pub/loginSms")
    public CommonResult localHostLogin(@RequestParam String phoneNumber){
        ShopOwner shopOwner = new ShopOwner();
        shopOwner.setUuid(UUIDUtils.getUUID());
        shopOwner.setPhone(phoneNumber);
        shopOwner.setName(phoneNumber);
        shopOwnerService.save(shopOwner);
        return CommonResult.ok();
    }

    /**
     * 先调用验证码登录接口，
     * 拿到登录用户信息token，
     * 传入后台获取手机号码
     */
    @ApiOperation("重置密码")
    @PostMapping("/pub/resetPassword")
    public CommonResult resetPassword(@RequestBody PassVo passVo){
        ValidatorUtils.validateEntity(passVo);//校验密码
        UserDto currentUser = CurrentUserHolder.getCurrentUser();
        String phone = currentUser.getPhone();//获取电话号码
        boolean update = shopOwnerService.update(new UpdateWrapper<ShopOwner>().eq("phone", phone).set("password", passVo.getPassword()));
        return CommonResult.ok(update);
    }

}
