package com.jsy.controller;
import com.jsy.basic.util.PageInfo;
import com.jsy.dto.userCollectDto;
import com.jsy.param.UserCollectParam;
import com.jsy.service.IUserCollectService;
import com.jsy.domain.UserCollect;
import com.jsy.query.UserCollectQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

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
    public CommonResult addUserCollect(@RequestBody UserCollectParam userCollectParam){
        userCollectService.addUserCollect(userCollectParam);
        return CommonResult.ok();
    }

    /**
    * 删除一条收藏记录
    * @param id
    * @return
    */
    @DeleteMapping(value="/delUserCollect")
    public CommonResult delUserCollect(@RequestParam("id") Long id){
        userCollectService.removeById(id);
        return CommonResult.ok();
    }

    /**
    * 分页查询收藏的商品、服务、套餐、店铺
    * @param userCollectQuery 查询对象
    * @return PageList 分页对象
    */
    @PostMapping(value = "/userCollectPageList")
    public CommonResult<userCollectDto> userCollectPageList(@RequestBody UserCollectQuery userCollectQuery)
    {
        userCollectDto userCollectDto= userCollectService.userCollectPageList(userCollectQuery);
        return CommonResult.ok(userCollectDto);
    }
}
