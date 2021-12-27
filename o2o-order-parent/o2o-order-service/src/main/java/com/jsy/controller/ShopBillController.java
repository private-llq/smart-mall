package com.jsy.controller;
import com.jsy.dto.SelectBillDto;
import com.jsy.dto.SelectSevenBillDto;
import com.jsy.query.*;
import com.jsy.service.IShopBillService;
import com.jsy.domain.ShopBill;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shopBill")
public class ShopBillController {
    @Autowired
    public IShopBillService shopBillService;
    //添加一条收入或者退款记录
    @PostMapping (value="/addShopBillOne")
    public CommonResult<Boolean>  addShopBillOne(@RequestBody AddShopBillOneParam param){
        Boolean  b =  shopBillService.addShopBillOne(param);
        return new CommonResult<>(200,"添加成功",b);
    }

    //分页查询店铺收入、退款记录
    @PostMapping(value="/selectBill")
    public  CommonResult<List<SelectBillDto>> selectBill(@RequestBody SelectBillParam param){
        List<SelectBillDto>  vo =  shopBillService.selectBill(param);
        return new CommonResult<>(200,"查询成功",vo);
    }
    //查询某一天店铺收入、退款记录
    @PostMapping(value="/selectBillOneDay")
    public  CommonResult<SelectBillDto> selectBillOneDay(@RequestBody SelectBillOneDayParam param){
        SelectBillDto  vo =  shopBillService.selectBillOneDay(param);
        return new CommonResult<>(200,"查询成功",vo);
    }
    //查询某一时间段店铺收入、退款记录
    @PostMapping(value="/selectBillSomeTime")
    public  CommonResult<List<SelectBillDto>> selectBillSomeTime(@RequestBody SelectBillSomeTimeParam param){
        List<SelectBillDto>  vo =  shopBillService.selectBillSomeTime(param);
        return new CommonResult<>(200,"查询成功",vo);
    }
    //近N日成交额：统计最近N天完成交易的订单（不包括退款售后订单金额）
    @PostMapping  (value="/selectSevenBill")
    public  CommonResult<SelectSevenBillDto> selectSevenBill(@RequestBody SelectSevenBillParam param ){
        SelectSevenBillDto  vo =  shopBillService.selectSevenBill(param);
        return new CommonResult<>(200,"查询成功",vo);
    }



}
