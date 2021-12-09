package com.jsy.controller;
import com.jsy.basic.util.PageInfo;
import com.jsy.domain.UserCollect;
import com.jsy.dto.userCollectDto;
import com.jsy.param.UserCollectParam;
import com.jsy.service.IUserCollectService;
import com.jsy.query.UserCollectQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

@RestController
@RequestMapping("/userCollect")
public class UserCollectController {
    @Autowired
    public IUserCollectService userCollectService;

    /**
     * 收藏商品\服务\套餐\店铺
     * @param userCollectParam
     * @return
     */
    @PostMapping(value="/addUserCollect")
    public CommonResult<UserCollect> addUserCollect(@RequestBody UserCollectParam userCollectParam){
        UserCollect userCollect = userCollectService.addUserCollect(userCollectParam);
        return CommonResult.ok(userCollect);
    }

    /**
     * 收藏按钮状态 亮(已收藏)：true   灰色 ：false
     */
    @PostMapping(value="/userCollectState")
    public CommonResult userCollectState(@RequestParam("type") Integer type,@RequestParam("id") Long id){
        Boolean state= userCollectService.userCollectState(type,id);
        return CommonResult.ok(state);
    }

    /**
    * 列表删除一条收藏记录
    * @param id
    * @return
    */
    @DeleteMapping(value="/delUserCollect")
    public CommonResult delUserCollect(@RequestParam("id") Long id){
        userCollectService.removeById(id);
        return CommonResult.ok();
    }

    /**
     * 取消收藏
     */
    @DeleteMapping(value="/cancelUserCollect")
    public CommonResult cancelUserCollect(@RequestParam("type") Integer type,@RequestParam("id") Long id){
        userCollectService.cancelUserCollect(type,id);
        return CommonResult.ok();
    }

    /**
    * 分页查询收藏的商品、服务、套餐、店铺
    * @param userCollectQuery 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/userCollectPageList")
    public CommonResult<PageInfo<UserCollect>> userCollectPageList(@RequestBody UserCollectQuery userCollectQuery)
    {
        PageInfo<UserCollect> pageInfo=userCollectService.userCollectPageList(userCollectQuery);
        return CommonResult.ok(pageInfo);
    }
}
