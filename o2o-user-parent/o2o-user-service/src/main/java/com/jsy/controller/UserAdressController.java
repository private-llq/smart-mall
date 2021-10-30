package com.jsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.utils.RegexUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.UserAddress;
import com.jsy.service.IUserAddressService;
import com.jsy.vo.UserAddressVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/userAddress")
@Api(tags = "用户地址")
public class UserAdressController {

    @Autowired
    public IUserAddressService userAdressService;

    /**
     * 保存
     * @param userAddressVO 传递的实体
     * @return Ajaxresult 转换结果
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public CommonResult save(@RequestBody UserAddressVO userAddressVO) {
        String phone = userAddressVO.getPhone();
        if (!RegexUtils.isMobile(phone)){
            return CommonResult.error(-1,"手机号不正确");
        }
        userAdressService.save(userAddressVO);
        return CommonResult.ok();
    }

    /**
     *
     * 修改
     * @param userAddressVO
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public CommonResult update(@RequestBody UserAddressVO userAddressVO){
        userAdressService.updateByUuId(userAddressVO);
        return CommonResult.ok();
    }

    /**
     * 通过uuid查询地址
     * @param uuid
     * @return
     */
    @RequestMapping(value = "/getByUuid/{uuid}",method = RequestMethod.GET)
    public CommonResult<UserAddress> getByUuid(@PathVariable("uuid")String uuid) {
        UserAddress userAddress = userAdressService.getByUuid(uuid);
        return CommonResult.ok(userAddress);
    }


    /**
     * 删除对象信息
     * @return
     */
    @RequestMapping(value = "/{uuid}",method = RequestMethod.DELETE)
    public CommonResult delete(@PathVariable("uuid") String uuid) {
          return CommonResult.ok(userAdressService.remove(new QueryWrapper<UserAddress>().eq("uuid",uuid)));
    }



    /**
     * 查看所有的信息
     * @return
     */
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public CommonResult list() {
        return CommonResult.ok(userAdressService.queryByUserUuid());
    }

}
