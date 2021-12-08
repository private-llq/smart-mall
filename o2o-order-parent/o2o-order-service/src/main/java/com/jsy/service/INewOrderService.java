package com.jsy.service;

import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.NewOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.SelectShopOrderDto;
import com.jsy.dto.SelectUserOrderDto;
import com.jsy.query.*;

import java.util.List;

/**
 * <p>
 * 订单表 服务类
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
public interface INewOrderService extends IService<NewOrder> {
    //新增一条订单
    Boolean creationOrder(CreationOrderParam creationOrderParam);
    //用户根据转态查询订单
    List<SelectUserOrderDto> selectUserOrder(Long id, SelectUserOrderParam param);
    //商家根据转态查询订单
    List<SelectShopOrderDto> selectShopOrder( SelectShopOrderParam param);
    //用户删除订单
    boolean deletedUserOrder(Long orderId);
    //商家同意预约订单
    Boolean consentOrder(Long shopId,Long orderId);
    //用户支付后调用的接口
    public Boolean completionPay(CompletionPayParam param);
    //商家验卷接口
    public boolean acceptanceCheck(Long shopId, String code,Long orderId);
    //商家根据验证码查询订单
    SelectShopOrderDto shopConsentOrder(ShopConsentOrderParam param);
    //根据订单id查询订单详情
    SelectShopOrderDto  selectOrderByOrderId(Long orderId);
    //支付宝支付
    CommonResult alipay(Long orderId);
}
