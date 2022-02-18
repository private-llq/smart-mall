package com.jsy.service;

import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.NewOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.*;
import com.jsy.query.*;
import com.jsy.vo.SelectAllOrderByBackstageVo;
import com.jsy.vo.SelectShopOrderVo;
import com.zhsj.base.api.domain.PayCallNotice;
import com.zhsj.basecommon.vo.R;


import java.util.ArrayList;
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
  //  List<SelectShopOrderDto> selectShopOrder( SelectShopOrderParam param);
    //用户删除订单
    boolean deletedUserOrder(Long orderId);
    //商家同意预约订单
    Boolean consentOrder(Long shopId,Long orderId);
    //用户支付后调用的接口
    public Boolean completionPay(CompletionPayParam param);
    //回调接口修改版
    Boolean replyPayOne(R<PayCallNotice> param);
    //商家验卷接口
    public boolean acceptanceCheck(Long shopId, String code,Long orderId);
    //商家根据验证码查询订单
    SelectShopOrderDto shopConsentOrder(ShopConsentOrderParam param);
    //根据订单id查询订单详情
    SelectShopOrderDto  selectOrderByOrderId(Long orderId);
    //支付宝支付
    CommonResult alipay(Long orderId);
    //微信支付
    CommonResult WeChatPay(Long orderId);
    //退款接口
    Boolean allPayRefund(Long orderId);
    //创建订单接口返回订单id
    Long insterOrder(InsterOrderParam param);
    //创建订单接口返回订单id(单个商品)
    Long insterOrderOne(InsterOrderOneParam param);
    //查询近多少日订单量
    OrderSizeDto orderSize(OrderSizeParam param);
    //修改生成订单商品数量（单个商品直接购买）
    Boolean updateOrderOne(UpdateOrderOneParam param);
    //查询相应状态下的数量
    ArrayList<SelectUserOrderNumberDto> selectUserOrderNumber(Long id);
    //大后台查询所有订单
    List<SelectAllOrderByBackstageDto> selectAllOrderByBackstage(SelectAllOrderByBackstageParam param);
    //商家根据转态查询订单(包含逻辑删除)
    List<SelectShopOrderVo> selectShopOrder2(SelectShopOrderParam param);
    //商家根据转态查询订单数量(包含逻辑删除)
    Integer selectShopOrder2Count(SelectShopOrderParam param);
}
