package com.jsy.controller;
import com.jsy.dto.SelectWithdrawDto;
import com.jsy.dto.SelectWithdrawMonthDto;
import com.jsy.query.*;
import com.jsy.service.IShopWithdrawService;
import com.jsy.domain.ShopWithdraw;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/shopWithdraw")
public class ShopWithdrawController {
    @Autowired
    public IShopWithdrawService shopWithdrawService;

    //新增一条提现记录
    @PostMapping(value="/addShopWithdrawOne")
    public CommonResult<Boolean>  addShopWithdrawOne(@RequestBody AddShopWithdrawOneParam param){
        Boolean b  =  shopWithdrawService.addShopWithdrawOne(param);
        return new CommonResult<>(200,"新增一条提现记录成功",b);
    }


    //查询提现记录
    @PostMapping(value="/selectWithdraw")
    public CommonResult<List<SelectWithdrawDto>>  selectWithdraw(@RequestBody SelectWithdraParam param){
        List<SelectWithdrawDto> vo  =  shopWithdrawService.selectWithdraw(param);
        return new CommonResult<>(200,"查询提现记录成功",vo);
    }

    //查询某个月提现记录
    @PostMapping(value="/selectWithdrawMonth")
    public CommonResult<SelectWithdrawMonthDto>  selectWithdrawMonth(@RequestBody SelectWithdrawMonthParam param){
        SelectWithdrawMonthDto vo  =  shopWithdrawService.selectWithdrawMonth(param);
        return new CommonResult<>(200,"查询提现记录成功",vo);
    }


}
