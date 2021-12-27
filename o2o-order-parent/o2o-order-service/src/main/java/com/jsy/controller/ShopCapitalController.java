package com.jsy.controller;
import com.jsy.query.AddCapitalParam;
import com.jsy.service.IShopCapitalService;
import com.jsy.domain.ShopCapital;
import com.jsy.query.ShopCapitalQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/shopCapital")
public class ShopCapitalController {
    @Autowired
    public IShopCapitalService shopCapitalService;

    @PostMapping(value="/addCapital")
    //增加余额
    public CommonResult<Boolean>  addCapital(@RequestBody AddCapitalParam param){
        Boolean b  =  shopCapitalService.addCapital(param);
        return new CommonResult<>(200,"添加成功",b);
    }
    //减少余额
    @PostMapping(value="/subtractCapital")
    public CommonResult<Boolean>  subtractCapital(@RequestBody AddCapitalParam param){
        Boolean b  =  shopCapitalService.subtractCapital(param);
        return new CommonResult<>(200,"减少成功",b);
    }

    //初始化店铺余额
    @GetMapping(value="/initializeShopCapital")
    public CommonResult<Boolean>  initializeShopCapital(@RequestParam("shopId") @NotNull Long shopId){
        Boolean b  =  shopCapitalService.initializeShopCapital(shopId);
        return new CommonResult<>(200,"创建商家余额账户成功",b);
    }

    //查询店铺的余额
    @GetMapping(value="/selectShopCapital")
    public CommonResult<ShopCapital> selectShopCapital(@RequestParam("shopId") @NotNull Long shopId){
        ShopCapital shopCapital=    shopCapitalService.selectShopCapital(shopId);
        return new CommonResult<>(200,"查询成功",shopCapital);
    }
}
