package com.jsy.controller;

import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.config.HttpClientHelper;
import com.jsy.dto.*;
import com.jsy.query.*;
import com.jsy.service.INewOrderService;
import com.jsy.domain.NewOrder;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jsy.utils.AliAppPayQO;

import com.jsy.vo.SelectAllOrderByBackstageVo;
import com.zhsj.base.api.domain.PayCallNotice;
import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.annotation.LoginIgnore;
import com.zhsj.baseweb.support.ContextHolder;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/newOrder")
public class NewOrderController {
    @Autowired
    public INewOrderService newOrderService;


    /**********************************************arli***************************************/

//    @ApiOperation("新增订单")
//    @RequestMapping(value = "/creationOrder", method = RequestMethod.POST)
//    public CommonResult<Boolean> creationOrder(@RequestBody CreationOrderParam creationOrderParam) {
//        Boolean b = newOrderService.creationOrder(creationOrderParam);
//        return new CommonResult<Boolean>(200, "新增订单成功", b);
//    }
    @ApiOperation("生成订单接口返回订单id")
    @RequestMapping(value = "/insterOrder", method = RequestMethod.POST)
    public CommonResult<String> insterOrder(@RequestBody InsterOrderParam param) {
           Long orderId =  newOrderService.insterOrder(param);
        return new CommonResult<String>(200, "新增订单成功", orderId.toString());
    }

    @ApiOperation("生成订单接口返回订单id（单个商品直接购买）")
    @RequestMapping(value = "/insterOrderOne", method = RequestMethod.POST)
    public CommonResult<String> insterOrderOne(@RequestBody InsterOrderOneParam param) {
        Long orderId =  newOrderService.insterOrderOne(param);
        return new CommonResult<String>(200, "新增订单成功", orderId.toString());
    }
    @ApiOperation("修改生成订单商品数量（单个商品直接购买）+-")
    @RequestMapping(value = "/updateOrderOne", method = RequestMethod.POST)
    public CommonResult<Boolean> updateOrderOne(@RequestBody UpdateOrderOneParam param) {
        Boolean b =  newOrderService.updateOrderOne(param);
        if(b ){
            return new CommonResult<Boolean>(200, "修改成功", b);
        }
        return new CommonResult<Boolean>(200, "修改失败", b);
    }


    @ApiOperation("用户根据转态查询订单")
    @RequestMapping(value = "/selectUserOrder", method = RequestMethod.POST)
    public CommonResult<PagerUtils> selectUserOrder(@RequestBody SelectUserOrderParam param) {
        Long id = ContextHolder.getContext().getLoginUser().getId();//获取用户id
        String token = ContextHolder.getContext().getLoginUser().getToken();
        log.info("token"+token);

        System.out.println("用户id"+id);
        List<SelectUserOrderDto> list = newOrderService.selectUserOrder(id, param);
        PagerUtils pagerUtils = new PagerUtils<SelectUserOrderDto>();
        PagerUtils pagerUtils1 = pagerUtils.queryPage(param.getPage(), param.getSize(), list);
        return new CommonResult<>(200, "查询成功", pagerUtils1);
    }
    @ApiOperation("查询相应状态下的数量")
    @RequestMapping(value = "/selectUserOrderNumber", method = RequestMethod.GET)
    public CommonResult<ArrayList<SelectUserOrderNumberDto>> selectUserOrderNumber() {

        Long id = ContextHolder.getContext().getLoginUser().getId();//获取用户id
        ArrayList<SelectUserOrderNumberDto>  selectUserOrderNumberDtos= newOrderService.selectUserOrderNumber(id);
        return new CommonResult<>(200,"查询成功",selectUserOrderNumberDtos);

    }







    @ApiOperation("商家根据转态查询订单")
    @RequestMapping(value = "/selectShopOrder", method = RequestMethod.POST)
    public CommonResult<PagerUtils> selectShopOrder(@RequestBody SelectShopOrderParam param) {
        List<SelectShopOrderDto> list = newOrderService.selectShopOrder(param);
        PagerUtils pagerUtils = new PagerUtils<SelectUserOrderDto>();
        PagerUtils pagerUtils1 = pagerUtils.queryPage(param.getPage(), param.getSize(), list);
        return new CommonResult<>(200, "查询成功", pagerUtils1);
    }

    @ApiOperation("用户删除订单")
    @RequestMapping(value = "/deletedUserOrder", method = RequestMethod.GET)
    public CommonResult<Boolean> deletedUserOrder(@RequestParam("orderId") Long orderId) {
        boolean b = newOrderService.deletedUserOrder(orderId);
        return new CommonResult<>(200, "删除成功", b);
    }

    @ApiOperation("商家根据验证码查询订单")
    @RequestMapping(value = "/shopConsentOrder", method = RequestMethod.POST)
    public CommonResult<SelectShopOrderDto> shopConsentOrder(@RequestBody ShopConsentOrderParam param) {
        SelectShopOrderDto value = newOrderService.shopConsentOrder(param);
        return CommonResult.ok(value);
    }
    @ApiOperation("商家同意预约订单")
    @RequestMapping(value = "/consentOrder", method = RequestMethod.POST)
    public CommonResult<Boolean> consentOrder(@RequestBody ConsentOrderParam param) {
        Boolean b = newOrderService.consentOrder(param.getShopId(), param.getOrderId());
        if (b) {
            return new CommonResult<>(200, "商家接受预约", b);
        }
        return new CommonResult<>(500, "未成功", b);
    }

    @ApiOperation("商家验卷接口")
    @RequestMapping(value = "/acceptanceCheck", method = RequestMethod.POST)
    public CommonResult<Boolean> acceptanceCheck(@RequestBody AcceptanceCheckParam param) {
        boolean b = newOrderService.acceptanceCheck(param.getShopId(), param.getCode(), param.getOrderId());
        if (b) {
            return new CommonResult<>(200, "验证成功", b);
        }
        return new CommonResult<>(500, "验证失败", b);
    }
    @ApiOperation("根据订单id查询订单详情")
    @RequestMapping(value = "/selectOrderByOrderId", method = RequestMethod.GET)
    public CommonResult<SelectShopOrderDto> selectOrderByOrderId(@RequestParam("orderId") Long orderId) {
        SelectShopOrderDto shopOrderDto = newOrderService.selectOrderByOrderId(orderId);
        return new CommonResult<>(200, "查询成功", shopOrderDto);
    }

    @ApiOperation("支付宝支付接口")
    @RequestMapping(value = "/alipay", method = RequestMethod.GET)
    public CommonResult<String> alipay(@RequestParam("orderId") Long orderId) {
        CommonResult value=newOrderService.alipay(orderId);
        return value;
    }
//    @ApiOperation("支付宝退款接口")
//    @RequestMapping(value = "/alipayRefund", method = RequestMethod.GET)
//    public CommonResult<String> alipayRefund(@RequestParam("orderId") Long orderId) {
//        CommonResult value=newOrderService.alipayRefund(orderId);
//        return value;
//    }
    @ApiOperation("微信支付接口")
    @RequestMapping(value = "/WeChatPay", method = RequestMethod.GET)
    public CommonResult<String> WeChatPay(@RequestParam("orderId") Long orderId) {
        CommonResult value=newOrderService.WeChatPay(orderId);
        return value;
    }
    @ApiOperation("退款接口")
    @RequestMapping(value = "/allPayRefund", method = RequestMethod.GET)
    public CommonResult<Boolean> allPayRefund(@RequestParam("orderId") Long orderId) {
        Boolean value=newOrderService.allPayRefund(orderId);
        if(value){
            return new CommonResult<Boolean>(200,"退款成功",value) ;
        }
        return new CommonResult<Boolean>(200,"退款失败",value) ;

    }
//    @LoginIgnore
//    @ApiOperation("测试支付回调")
//    @RequestMapping(value = "/replyPay", method = RequestMethod.POST)
//    public CommonResult<Boolean> replyPay(@RequestBody CompletionPayParam param) {
//        log.info("回调成功");
//        System.out.println(param.toString());
//        Boolean b = newOrderService.completionPay(param);
//        if(b){
//            return new CommonResult<>(0, "回调成功", b);
//        }
//        return new CommonResult<>(1, "失败", b);
//    }



    @LoginIgnore
    @ApiOperation("测试支付回调")
    @RequestMapping(value = "/replyPay", method = RequestMethod.POST)
    public CommonResult<Boolean> replyPay(@RequestBody R<PayCallNotice> param) {
        log.info("进入回调成功");
        Boolean b =  newOrderService.replyPayOne(param);
                if(b){
            return new CommonResult<>(0, "回调成功", b);
        }
        return new CommonResult<>(1, "失败", b);

    }


    @ApiOperation("查询近多少日订单量")
    @RequestMapping(value = "/orderSize", method = RequestMethod.POST)
    public CommonResult<OrderSizeDto> orderSize(@RequestBody OrderSizeParam param){
        OrderSizeDto   dto= newOrderService.orderSize(param);
        return new CommonResult<>(200, "查询成功", dto);
    }




    //大后台查询所有订单
    @RequestMapping(value = "/selectAllOrderByBackstage", method = RequestMethod.POST)
    public CommonResult< List<SelectAllOrderByBackstageDto>> selectAllOrderByBackstage(@RequestBody SelectAllOrderByBackstageParam param){
        List<SelectAllOrderByBackstageDto>   dto= newOrderService.selectAllOrderByBackstage(param);
        return new CommonResult<>(200, "查询成功", dto);
    }

//    //根据订单号查询订单
//    @RequestMapping(value = "/selectOrderByBackstageNo", method = RequestMethod.GET)
//    public CommonResult<PagerUtils> selectOrderByBackstageNum(@RequestParam("orderNumber") String  orderNumber){
//        PagerUtils   dto= newOrderService.selectOrderByBackstageNum(param);
//        return new CommonResult<>(200, "查询成功", null);
//    }




}
