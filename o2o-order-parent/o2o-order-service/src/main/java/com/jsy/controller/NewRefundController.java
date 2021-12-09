package com.jsy.controller;
import com.jsy.dto.SelectRefundByoderDto;
import com.jsy.query.AgreeRefundParam;
import com.jsy.query.ApplyRefundParam;
import com.jsy.query.ShopWhetherRefundParam;
import com.jsy.service.INewRefundService;
import com.jsy.domain.NewRefund;
import com.jsy.query.NewRefundQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;
import java.util.List;

@RestController
@RequestMapping("/newRefund")
public class NewRefundController {
    @Autowired
    public INewRefundService newRefundService;

    @ApiOperation("申请退款")
    @RequestMapping(value = "/applyRefund", method = RequestMethod.POST)
    public CommonResult<Boolean>  applyRefund(@RequestBody ApplyRefundParam param){
        Boolean   b= newRefundService.applyRefund(param);
        if(b){
            return new CommonResult<>(200, "操作成功", b);
        }
        return new CommonResult<>(500, "操作失败", b);
    }

    @ApiOperation("根据订单号查询退款信息")
    @RequestMapping(value = "/selectRefundByoder", method = RequestMethod.GET)
    public CommonResult<SelectRefundByoderDto>  selectRefundByoder(@RequestParam("orderId") Long orderId){
        SelectRefundByoderDto dto =  newRefundService.selectRefundByoder(orderId);
        return new CommonResult<>(500, "操作成功",dto);
    }

    @ApiOperation("拒绝退款")
    @RequestMapping(value = "/refuseRefund", method = RequestMethod.POST)
    public CommonResult<Boolean>  refuseRefund(@RequestBody ShopWhetherRefundParam param){
        Boolean b  =newRefundService.refuseRefund(param);
        if(b){
            return new CommonResult<>(200, "操作成功",b);
        }
        return new CommonResult<>(500, "操作失败",b);
    }

    @ApiOperation("同意退款")
    @RequestMapping(value = "/agreeRefund", method = RequestMethod.POST)
    public CommonResult<Boolean>  agreeRefund(@RequestBody AgreeRefundParam param){
        Boolean   b= newRefundService.agreeRefund(param);
        if(b){
            return new CommonResult<>(200, "操作成功",b);
        }
        return new CommonResult<>(500, "操作失败",b);
    }


}
