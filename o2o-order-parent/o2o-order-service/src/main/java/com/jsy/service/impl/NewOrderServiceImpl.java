package com.jsy.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CodeUtils;
import com.jsy.basic.util.utils.OrderNoUtil;
import com.jsy.basic.util.utils.PagerUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.GoodsClient;
import com.jsy.client.ServiceCharacteristicsClient;
import com.jsy.client.SetMenuClient;
import com.jsy.client.ShoppingCartClient;
import com.jsy.config.*;
import com.jsy.domain.*;
import com.jsy.dto.*;
import com.jsy.mapper.*;
import com.jsy.parameter.ShoppingCartParam;
import com.jsy.query.*;
import com.jsy.service.INewOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.utils.AliAppPayQO;
import com.jsy.utils.Random8;
import com.jsy.utils.WeChatPayQO;
import com.jsy.vo.OrderGoodsVO;
import com.jsy.vo.OrderServiceVO;
import com.jsy.vo.OrderSetMenuVO;
import com.jsy.vo.SelectAllOrderByBackstageVo;
import com.zhsj.base.api.constant.RpcConst;
import com.zhsj.base.api.domain.PayCallNotice;
import com.zhsj.base.api.rpc.IBasePayRpcService;
import com.zhsj.base.api.rpc.IBaseUserInfoRpcService;
import com.zhsj.basecommon.vo.R;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.checkerframework.common.value.qual.DoubleVal;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
@Service
@Slf4j
public class NewOrderServiceImpl extends ServiceImpl<NewOrderMapper, NewOrder> implements INewOrderService {

    @Resource
    private NewOrderMapper newOrderMapper;
    @Resource
    private OrderServiceMapper orderServiceMapper;
    @Resource
    private OrderGoodsMapper orderGoodsMapper;
    @Resource
    private OrderSetMenuMapper orderSetMenuMapper;
    @Resource
    private OrderSetMenuGoodsMapper orderSetMenuGoodsMapper;
    @Resource
    private SetMenuClient setMenuClient;
    @Resource
    private GoodsClient goodsClient;
    @Resource
    private ShoppingCartClient shoppingCartClient;
    @Autowired
    private ServiceCharacteristicsClient characteristicsClient;

    @DubboReference(version = RpcConst.Rpc.VERSION, group = RpcConst.Rpc.Group.GROUP_BASE_USER, check = false)
    private IBasePayRpcService basePayRpcService;





    @Value("${pay.urlAliPay}")
    private String urlAliPay;//支付宝支付url
    @Value("${pay.urlWeChatPay}")
    private String urlWeChatPay;//微信支付url
    @Value("${pay.urlAliRefund}")
    private String urlAliRefund;//支付宝退款url
    @Value("${pay.urlWeChatRefund}")
    private String urlWeChatRefund;//微信退款url
    @Value("${pay.urlWeChatRefundStatus}")
    private String urlWeChatRefundStatus;//查询微信退款状态url


    /**
     * @Description 新增一条订单
     * @Param [creationOrderParam]
     * @Return java.lang.Boolean
     * @Author arli
     * @Date 2021/11/15
     * @Time 10:45
     */
    @Override
    @Transactional
    public Boolean creationOrder(CreationOrderParam creationOrderParam) {
        Long shopId = creationOrderParam.getShopId();//店铺id
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Long userId = loginUser.getId();//获取用户id
        try {
            NewOrder newOrder = new NewOrder();
            // newOrder.setOrderNum(OrderNoUtil.getOrder());//订单号
            if (creationOrderParam.getConsumptionWay() == 1) {//消费方式（1商家上门)
                newOrder.setAppointmentStatus(0);//预约状态（0预约中，1预约成功，2预约失败）
                BeanUtils.copyProperties(creationOrderParam, newOrder);
            }
            if (creationOrderParam.getConsumptionWay() == 0) {//消费方式（0用户到店)
                newOrder.setAppointmentStatus(1);//预约状态（0预约中，1预约成功，2预约失败）
                BeanUtils.copyProperties(creationOrderParam, newOrder);
                newOrder.setTelepone(loginUser.getPhone());//用户电话
            }
            newOrder.setUserId(userId);//用户id
            newOrder.setOrderStatus(1);//订单状态（[1待上门、待配送、待发货]，2、完成）
            newOrder.setPayStatus(0);//支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）
            newOrder.setCommentStatus(0);//是否评价0未评价，1评价（评价完成为订单完成）
            newOrder.setServerCodeStatus(0);//验卷状态0未验卷，1验卷成功
            int insert = newOrderMapper.insert(newOrder);
            if (creationOrderParam.getOrderType() == 0) {//服务订单
                List<CreationOrderServiceParam> orderServiceParams = creationOrderParam.getOrderServiceParams();
                for (CreationOrderServiceParam Param : orderServiceParams) {
                    OrderService orderService = new OrderService();
                    orderService.setOrderId(newOrder.getId());//订单id回填
                    BeanUtils.copyProperties(Param, orderService);
                    orderService.setShopId(shopId);//设置shopid
                    int insert1 = orderServiceMapper.insert(orderService);
                }
            }
            if (creationOrderParam.getOrderType() == 1) {//普通订单(包含单品和套餐)
                //单品
                if (creationOrderParam.getOrderGoodsParamS() != null) {
                    List<CreationOrderGoodsParam> orderGoodsParamS = creationOrderParam.getOrderGoodsParamS();
                    for (CreationOrderGoodsParam orderGoodsParam : orderGoodsParamS) {
                        OrderGoods orderGoods = new OrderGoods();
                        orderGoods.setOrderId(newOrder.getId());//订单id回填
                        BeanUtils.copyProperties(orderGoodsParam, orderGoods);
                        orderGoods.setShopId(shopId);//设置shopid
                        int insert1 = orderGoodsMapper.insert(orderGoods);
                    }
                }


                //套餐
                if (creationOrderParam.getOrderMenuParams() != null) {
                    List<CreationOrderMenuParam> orderMenuParams = creationOrderParam.getOrderMenuParams();
                    for (CreationOrderMenuParam orderMenuParam : orderMenuParams) {
                        OrderSetMenu orderSetMenu = new OrderSetMenu();
                        orderSetMenu.setOrderId(newOrder.getId());//订单id回填
                        BeanUtils.copyProperties(orderMenuParam, orderSetMenu);
                        orderSetMenu.setShopId(shopId);//设置shopid
                        int insert1 = orderSetMenuMapper.insert(orderSetMenu);
                        //套餐详情
                        List<CreationOrderMenuGoodsParam> creationOrderMenuGoodsParams = orderMenuParam.getCreationOrderMenuGoodsParams();
                        for (CreationOrderMenuGoodsParam creationOrderMenuGoodsParam : creationOrderMenuGoodsParams) {
                            OrderSetMenuGoods orderSetMenuGoods = new OrderSetMenuGoods();
                            orderSetMenuGoods.setOrderMenuId(orderSetMenu.getId());//套餐id回填
                            BeanUtils.copyProperties(creationOrderMenuGoodsParam, orderSetMenuGoods);
                            orderSetMenuGoods.setShopId(shopId);//设置shopid
                            int insert2 = orderSetMenuGoodsMapper.insert(orderSetMenuGoods);
                        }
                    }
                }
            }
        } catch (BeansException e) {
            e.printStackTrace();
        }
        return true;

    }

    //生成订单接口返回订单id
    @Override
    @Transactional
    @LcnTransaction
    public Long insterOrder(InsterOrderParam param) {
        Long shopId = param.getShopId();//店铺id
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Long userId = loginUser.getId();//获取用户id
        System.out.println("用户id" + userId);
        NewOrder newOrder = new NewOrder();//订单创建对象
        newOrder.setOrderNum(OrderNoUtil.getOrder());//订单号
        if (param.getConsumptionWay() == 1) {//消费方式（1商家上门)
            newOrder.setAppointmentStatus(0);//预约状态（0预约中，1预约成功，2预约失败）
            BeanUtils.copyProperties(param, newOrder);
        }
        if (param.getConsumptionWay() == 0) {//消费方式（0用户到店)
            newOrder.setAppointmentStatus(1);//预约状态（0预约中，1预约成功，2预约失败）
            BeanUtils.copyProperties(param, newOrder);
            newOrder.setTelepone(loginUser.getPhone());//用户电话
        }
        newOrder.setUserId(userId);//用户id
        newOrder.setOrderStatus(1);//订单状态（[1待上门、待配送、待发货]，2、完成）
        newOrder.setPayStatus(0);//支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）
        newOrder.setCommentStatus(0);//是否评价0未评价，1评价（评价完成为订单完成）
        newOrder.setServerCodeStatus(0);//验卷状态0未验卷，1验卷成功


        ShoppingCartParam shoppingCartParam = new ShoppingCartParam();//查询购物的参数对象
        shoppingCartParam.setShopId(shopId);//店铺id
        shoppingCartParam.setUserId(userId);//用户id
        ShoppingCartDto cartData = shoppingCartClient.queryCart(shoppingCartParam).getData();//查询购物车信息

        //验证最终金额
        BigDecimal payPrice = cartData.getPayPrice();//支付价格
        BigDecimal orderAllPrice = param.getOrderAllPrice();//订单总价
        System.out.println("支付价格" + payPrice + "订单总价" + orderAllPrice);
        int i = payPrice.compareTo(orderAllPrice);
        if (i != 0) {
            throw new JSYException(500, "总计不符");
        }

        if (cartData == null) {
            throw new JSYException(500, "没有购物车信息");
        }
        // BigDecimal sumPrice = cartData.getSumPrice();// 商品原价
        newOrder.setOrderAllPrice(payPrice);//订单的最终价格(购物车的支付价格)
        newOrder.setOrderAllNumber(cartData.getSumGoods());//订单总数量


        Integer shopType = cartData.getShopType();// 商店类型 服务行业：1是服务行业  0 套餐行业
        System.out.println("商店类型1是服务行业  0 套餐行业" + shopType);
        if (shopType == 1) {
            newOrder.setOrderType(0);//订单类型（0-服务类(只有服务)
        } else if (shopType == 0) {
            newOrder.setOrderType(1);//订单类型（1-普通类（套餐，单品集合）
        }
        int insert = newOrderMapper.insert(newOrder);
        Long orderId = newOrder.getId();//订单id
        List<ShoppingCartListDto> cartList = cartData.getCartList();//购物车详情

//        SelectShopOrderDto shopOrderDto = selectOrderByOrderId(orderId);
//        HashMap<String, Object> orderData = createOrderData(shopOrderDto);
//        NewOrder newOrder1 = new NewOrder();
//        newOrder1.setId(orderId);
//        newOrder1.setBillRise(orderData.toString());
//        int i1 = newOrderMapper.updateById(newOrder1);

        //添加订单的详情
        try {
            for (ShoppingCartListDto cartListDto : cartList) {

                if (cartListDto.getState() == false) {
                    throw new JSYException(500, "商品处于不正常状态");
                }

                Integer type = cartListDto.getType();//0：商品 1：服务  2：套餐
                Long goodsId = cartListDto.getGoodsId();//获取商品的id

                if (type == 0) {//商品
                    System.out.println("进入商品");
                    GoodsDto goodsDto = goodsClient.getGoods(goodsId).getData();////根据服务(商品)id查询商品详情
                    if (goodsDto == null) {
                        throw new JSYException(500, "无商品详情信息");
                    }
                    //将商品添加进订单详情
                    OrderGoods orderGoods = new OrderGoods();
                    orderGoods.setOrderId(orderId);//订单id回填
                    orderGoods.setShopId(shopId);//设置shopid

                    orderGoods.setGoodsTypeId(goodsDto.getGoodsTypeId());//商品的类型id
                    orderGoods.setTextDescription(goodsDto.getTextDescription());//商品描述

                    orderGoods.setAmount(cartListDto.getNum());//商品的数量
                    orderGoods.setGoodsId(cartListDto.getGoodsId());//商品id
                    orderGoods.setDiscountPrice(cartListDto.getDiscountPrice());//折扣价格
                    orderGoods.setDiscountState(cartListDto.getDiscountState());//折扣状态
                    orderGoods.setImages(cartListDto.getImages());//图片
                    orderGoods.setTitle(cartListDto.getTitle());//商品名称
                    orderGoods.setPrice(cartListDto.getPrice());//原价

                    int insert1 = orderGoodsMapper.insert(orderGoods);


                }
                if (type == 1 && shopType == 1) {//服务
                    System.out.println("进去服务");
                    GoodsServiceDto data = goodsClient.getGoodsService(goodsId).getData();//根据服务(商品)id查询服务详情
                    if (data == null) {
                        throw new JSYException(500, "无服务详情信息");
                    }

                    OrderService orderService = new OrderService();
                    orderService.setOrderId(orderId);//订单id回填
                    orderService.setShopId(shopId);//设置shopid

                    orderService.setAmount(cartListDto.getNum());//数量
                    orderService.setPrice(cartListDto.getPrice());//价格
                    orderService.setImages(cartListDto.getImages());//图片
                    orderService.setServiceId(cartListDto.getGoodsId());//服务id
                    orderService.setDiscountState(cartListDto.getDiscountState());//折扣状态
                    orderService.setDiscountPrice(cartListDto.getDiscountPrice());//折扣价

                    orderService.setGoodsTypeId(data.getGoodsTypeId());//服务分类id
                    orderService.setServiceRegulations(data.getServiceRegulations());//使用规则
                    orderService.setPhone(data.getServiceCall());//服务电话
                    orderService.setTitle(data.getTitle());//标题
                    orderService.setTextDescription(data.getTextDescription());//服务备注
                    orderService.setValidUntilTime(data.getValidUntilTime());//服务有效期

                    int insert1 = orderServiceMapper.insert(orderService);


                }
                if (type == 2 && shopType == 0) {//套餐
                    System.out.println("进入套餐");
                    Long setMenuId = cartListDto.getSetMenuId();//获取套餐的id
                    SetMenuDto menuData = setMenuClient.SetMenuList(setMenuId).getData();//根据套餐id查询套餐详情
                    if (menuData == null) {
                        throw new JSYException(500, "无套餐详情信息");
                    }
                    OrderSetMenu orderSetMenu = new OrderSetMenu();
                    orderSetMenu.setOrderId(orderId);//订单id回填
                    orderSetMenu.setShopId(shopId);//店铺id


                    orderSetMenu.setMenuId(cartListDto.getSetMenuId());//套餐id
                    orderSetMenu.setAmount(cartListDto.getNum());//套餐数量
                    orderSetMenu.setRealPrice(cartListDto.getPrice());//原价
                    orderSetMenu.setSellingPrice(cartListDto.getDiscountPrice());//折扣价格
                    orderSetMenu.setDiscountState(cartListDto.getDiscountState());//折扣状态

                    orderSetMenu.setMenuExplain(menuData.getMenuExplain());//规则说明
                    orderSetMenu.setName(menuData.getName());//套餐名称
                    orderSetMenu.setImages(menuData.getImages());//套餐图片
                    int insert1 = orderSetMenuMapper.insert(orderSetMenu);

                    List<SetMenuListDto> map = menuData.getMap();//套餐详情

                    for (SetMenuListDto setMenuListDto : map) {//循环套餐详情
                        List<SetMenuGoodsDto> goods = setMenuListDto.getDtoList();
                        for (SetMenuGoodsDto good : goods) {

                            OrderSetMenuGoods orderSetMenuGoods = new OrderSetMenuGoods();
                            orderSetMenuGoods.setGoodsId(good.getId());
                            orderSetMenuGoods.setShopId(shopId);//设置shopid
                            orderSetMenuGoods.setOrderMenuId(orderSetMenu.getId());//套餐id回填
                            orderSetMenuGoods.setAmount(good.getAmount());//数量
                            // orderSetMenuGoods.setGoodsTypeId();无
                            //orderSetMenuGoods.setImages();无
                            orderSetMenuGoods.setPrice(good.getPrice());//价格
                            orderSetMenuGoods.setTitle(good.getName());//商品的名称
                            int insert2 = orderSetMenuGoodsMapper.insert(orderSetMenuGoods);
                        }
                    }

                }

            }
        } catch (JSYException e) {
            throw new JSYException(500, e.getMessage());
        } catch (BeansException e) {
            e.printStackTrace();
        }


        int code = shoppingCartClient.clearCart(shoppingCartParam).getCode();//0代表清除成功
        if (code != 0) {
            throw new JSYException(500, "购物车清除失败");
        }


        return orderId;
    }

    //生成订单接口返回订单id单个商品
    @Override
    @Transactional
    public Long insterOrderOne(InsterOrderOneParam param) {
        Long shopId = param.getShopId();//店铺id
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Long userId = loginUser.getId();//获取用户id
        System.out.println("用户id" + userId);
        NewOrder newOrder = new NewOrder();//订单创建对象
        // newOrder.setOrderNum(OrderNoUtil.getOrder());//订单号
        if (param.getConsumptionWay() == 1) {//消费方式（1商家上门)
            newOrder.setAppointmentStatus(0);//预约状态（0预约中，1预约成功，2预约失败）
            BeanUtils.copyProperties(param, newOrder);
        }
        if (param.getConsumptionWay() == 0) {//消费方式（0用户到店)
            newOrder.setAppointmentStatus(1);//预约状态（0预约中，1预约成功，2预约失败）
            BeanUtils.copyProperties(param, newOrder);
            newOrder.setTelepone(loginUser.getPhone());//用户电话
        }
        newOrder.setOrderAllNumber(param.getNumber());
        newOrder.setUserId(userId);//用户id
        newOrder.setOrderStatus(1);//订单状态（[1待上门、待配送、待发货]，2、完成）
        newOrder.setPayStatus(0);//支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）
        newOrder.setCommentStatus(0);//是否评价0未评价，1评价（评价完成为订单完成）
        newOrder.setServerCodeStatus(0);//验卷状态0未验卷，1验卷成功
        Integer shopType = param.getShopType();//店铺类型
        System.out.println("商店类型1是服务行业  0 套餐行业" + shopType);
        if (shopType == 1) {
            newOrder.setOrderType(0);//订单类型（0-服务类(只有服务)
        } else if (shopType == 0) {
            newOrder.setOrderType(1);//订单类型（1-普通类（套餐，单品集合）
        }
        int insert = newOrderMapper.insert(newOrder);
        Long orderId = newOrder.getId();//订单id

        Integer typeByGoods = param.getTypeByGoods();//类型
        Integer number = param.getNumber();//数量
        Long typeByGoodsId = param.getGoodsId();//id

        if (typeByGoods == 0) {//商品
            GoodsDto goodsDto = goodsClient.getGoods(typeByGoodsId).getData();//根据服务(商品)id查询商品详情
            if (goodsDto == null) {
                throw new JSYException(500, "无商品详情信息");
            }
            //验证价格
            BigDecimal payPrice = (goodsDto.getDiscountState() == 1 ? goodsDto.getDiscountPrice() : goodsDto.getPrice()).multiply(new BigDecimal(number));
            ;//支付价格
            BigDecimal orderAllPrice = param.getOrderAllPrice();//订单总价
            System.out.println("支付价格" + payPrice + "订单总价" + orderAllPrice);
            int i = payPrice.compareTo(orderAllPrice);
            if (i != 0) {
                throw new JSYException(500, "总计不符");
            }
            //将商品添加进订单详情
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setOrderId(orderId);//订单id回填
            orderGoods.setShopId(shopId);//设置shopid
            orderGoods.setGoodsTypeId(goodsDto.getGoodsTypeId());//商品的类型id
            orderGoods.setTextDescription(goodsDto.getTextDescription());//商品描述
            orderGoods.setAmount(number);//商品的数量
            orderGoods.setGoodsId(goodsDto.getId());//商品id
            orderGoods.setDiscountPrice(goodsDto.getDiscountPrice());//折扣价格
            orderGoods.setDiscountState(goodsDto.getDiscountState());//折扣状态
            orderGoods.setImages(goodsDto.getImages());//图片
            orderGoods.setTitle(goodsDto.getTitle());//商品名称
            orderGoods.setPrice(goodsDto.getPrice());//原价
            int insert1 = orderGoodsMapper.insert(orderGoods);
        } else if (typeByGoods == 1) {//服务
            GoodsServiceDto goodsDto = goodsClient.getGoodsService(typeByGoodsId).getData();//根据服务(商品)id查询服务详情
            if (goodsDto == null) {
                throw new JSYException(500, "无服务详情信息");
            }
            //验证价格
            BigDecimal payPrice = (goodsDto.getDiscountState() == 1 ? goodsDto.getDiscountPrice() : goodsDto.getPrice()).multiply(new BigDecimal(number));//支付价格
            BigDecimal orderAllPrice = param.getOrderAllPrice();//订单总价
            System.out.println("支付价格" + payPrice + "订单总价" + orderAllPrice);
            int i = payPrice.compareTo(orderAllPrice);
            if (i != 0) {
                throw new JSYException(500, "总计不符");
            }
            OrderService orderService = new OrderService();
            orderService.setOrderId(orderId);//订单id回填
            orderService.setShopId(shopId);//设置shopid
            orderService.setAmount(number);//数量
            orderService.setPrice(goodsDto.getPrice());//价格
            orderService.setImages(goodsDto.getImages());//图片
            orderService.setServiceId(goodsDto.getId());//服务id
            orderService.setDiscountState(goodsDto.getDiscountState());//折扣状态
            orderService.setDiscountPrice(goodsDto.getDiscountPrice());//折扣价
            orderService.setGoodsTypeId(goodsDto.getGoodsTypeId());//服务分类id
            orderService.setServiceRegulations(goodsDto.getServiceRegulations());//使用规则
            orderService.setPhone(goodsDto.getServiceCall());//服务电话
            orderService.setTitle(goodsDto.getTitle());//标题
            orderService.setTextDescription(goodsDto.getTextDescription());//服务备注
            orderService.setValidUntilTime(goodsDto.getValidUntilTime());//服务有效期
            int insert2 = orderServiceMapper.insert(orderService);

        } else if (typeByGoods == 2) {//套餐
            SetMenuDto menuData = setMenuClient.SetMenuList(typeByGoodsId).getData();//根据套餐id查询套餐详情
            if (menuData == null) {
                throw new JSYException(500, "无套餐详情信息");
            }
            //验证价格
            BigDecimal payPrice = (menuData.getDiscountState() == 1 ? menuData.getSellingPrice() : menuData.getRealPrice()).multiply(new BigDecimal(number));//支付价格
            BigDecimal orderAllPrice = param.getOrderAllPrice();//订单总价
            System.out.println("支付价格" + payPrice + "订单总价" + orderAllPrice);
            int i = payPrice.compareTo(orderAllPrice);
            if (i != 0) {
                throw new JSYException(500, "总计不符");
            }

            OrderSetMenu orderSetMenu = new OrderSetMenu();
            orderSetMenu.setOrderId(orderId);//订单id回填
            orderSetMenu.setShopId(shopId);//店铺id
            orderSetMenu.setMenuId(menuData.getId());//套餐id
            orderSetMenu.setAmount(number);//套餐数量
            orderSetMenu.setRealPrice(menuData.getRealPrice());//原价
            orderSetMenu.setSellingPrice(menuData.getSellingPrice());//折扣价格
            orderSetMenu.setDiscountState(menuData.getDiscountState());//折扣状态
            orderSetMenu.setMenuExplain(menuData.getMenuExplain());//规则说明
            orderSetMenu.setName(menuData.getName());//套餐名称
            orderSetMenu.setImages(menuData.getImages());//套餐图片
            int insert3 = orderSetMenuMapper.insert(orderSetMenu);

            List<SetMenuListDto> map = menuData.getMap();//套餐详情

            for (SetMenuListDto setMenuListDto : map) {//循环套餐详情
                List<SetMenuGoodsDto> goods = setMenuListDto.getDtoList();
                for (SetMenuGoodsDto good : goods) {

                    OrderSetMenuGoods orderSetMenuGoods = new OrderSetMenuGoods();
                    orderSetMenuGoods.setGoodsId(good.getId());
                    orderSetMenuGoods.setShopId(shopId);//设置shopid
                    orderSetMenuGoods.setOrderMenuId(orderSetMenu.getId());//套餐id回填
                    orderSetMenuGoods.setAmount(good.getAmount());//数量
                    orderSetMenuGoods.setPrice(good.getPrice());//价格
                    orderSetMenuGoods.setTitle(good.getName());//商品的名称
                    int insert4 = orderSetMenuGoodsMapper.insert(orderSetMenuGoods);
                }
            }
        }
        return newOrder.getId();
    }

    //用户根据转态查询订单
    @Override
    public List<SelectUserOrderDto> selectUserOrder(Long id, SelectUserOrderParam param) {

        List<SelectUserOrderDto> listDto = new ArrayList<>();//返回对象

        QueryWrapper<NewOrder> queryWrapper = new QueryWrapper<>();

        Integer status = param.getStatus();//(0待受理,1已受理,2待消费,3已完成,4退款中)
        if (status == 0) {//0待受理
            queryWrapper.eq("user_id", id);//用户id
            queryWrapper.eq("appointment_status", 0);//预约状态（0预约中，1预约成功，2预约失败）
            queryWrapper.eq("order_status", 1);//订单状态（[1待上门、待配送、待发货]，2、完成）
            queryWrapper.eq("pay_status", 0);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            queryWrapper.eq("server_code_status", 0);//验卷状态0未验卷，1验卷成功
            queryWrapper.orderByDesc("update_time");
        }
        if (status == 1) {//1已受理
            queryWrapper.eq("user_id", id);//用户id
            queryWrapper.eq("appointment_status", 1);//预约状态（0预约中，1预约成功，2预约失败）
            queryWrapper.eq("order_status", 1);//订单状态（[1待上门、待配送、待发货]，2、完成）
            queryWrapper.eq("pay_status", 0);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            queryWrapper.eq("server_code_status", 0);//验卷状态0未验卷，1验卷成功
            queryWrapper.orderByDesc("update_time");
        }
        if (status == 2) {//2待消费
            queryWrapper.eq("user_id", id);//用户id
            queryWrapper.eq("appointment_status", 1);//预约状态（0预约中，1预约成功，2预约失败）
            queryWrapper.eq("pay_status", 1);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            queryWrapper.eq("server_code_status", 0);//验卷状态0未验卷，1验卷成功
            queryWrapper.orderByDesc("update_time");
        }

        if (status == 3) {//3针对页面上的已完成（已经使用了，验过卷）
            queryWrapper.eq("user_id", id);//用户id
            queryWrapper.eq("appointment_status", 1);//预约状态（0预约中，1预约成功，2预约失败）
            queryWrapper.eq("pay_status", 1);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            queryWrapper.eq("server_code_status", 1);//验卷状态0未验卷，1验卷成功
            queryWrapper.orderByDesc("update_time");
        }
        if (status == 4) {//4退款/售后
            queryWrapper
                    .eq("user_id", id)//用户id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    // .eq("server_code_status", 1)//验卷状态0未验卷，1验卷成功
                    .eq("pay_status", 2)//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
                    .orderByDesc("update_time")
                    .or()
                    .eq("user_id", id)//用户id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    .orderByDesc("update_time")
                    // .eq("server_code_status", 1)//验卷状态0未验卷，1验卷成功
                    .eq("pay_status", 3)//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
                    .or()
                    .orderByDesc("update_time")
                    .eq("user_id", id)//用户id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    //  .eq("server_code_status", 1)//验卷状态0未验卷，1验卷成功
                    .eq("pay_status", 4);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
        }
        if (status == 5) {//全部订单
            queryWrapper.eq("user_id", id);//用户id
            queryWrapper.orderByDesc("update_time");
        }

        List<NewOrder> newOrders = newOrderMapper.selectList(queryWrapper);//根据状态查询用户的所有订单
        if (newOrders.size() <= 0) {
           return null;
        }
        for (NewOrder newOrder : newOrders) {
            SelectUserOrderDto userOrderDTO = new SelectUserOrderDto();//单个订单返回对象
            BeanUtils.copyProperties(newOrder, userOrderDTO);
            List<SelectUserOrderServiceDto> serviceDtos = new ArrayList<>();
            List<SelectUserOrderGoodsDto> OrderGoodsDtos = new ArrayList<>();
            List<SelectUserOrderMenuDto> orderMenuDtos = new ArrayList<>();

            if (newOrder.getOrderType() == 0) {//（0-服务类(只有服务)
                QueryWrapper<OrderService> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("order_id", newOrder.getId());
                List<OrderService> orderServices = orderServiceMapper.selectList(queryWrapper1);//查服务订单详情
                for (OrderService orderService : orderServices) {
                    SelectUserOrderServiceDto serviceDto = new SelectUserOrderServiceDto();
                    BeanUtils.copyProperties(orderService, serviceDto);
                    serviceDtos.add(serviceDto);
                }

            }
            //单品集合
            QueryWrapper<OrderGoods> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("order_id", newOrder.getId());
            List<OrderGoods> orderGoods = orderGoodsMapper.selectList(queryWrapper1);//商品的集合
            for (OrderGoods orderGood : orderGoods) {
                SelectUserOrderGoodsDto orderGoodsDto = new SelectUserOrderGoodsDto();
                BeanUtils.copyProperties(orderGood, orderGoodsDto);
                OrderGoodsDtos.add(orderGoodsDto);
            }

            if (newOrder.getOrderType() == 1) {//1-普通类（套餐）
                QueryWrapper<OrderSetMenu> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("order_id", newOrder.getId());
                List<OrderSetMenu> orderSetMenus = orderSetMenuMapper.selectList(queryWrapper2);//套餐集合

                if (orderSetMenus.size() > 0) {

                    for (OrderSetMenu orderSetMenu : orderSetMenus) {
                        QueryWrapper<OrderSetMenuGoods> queryWrapper3 = new QueryWrapper<>();
                        queryWrapper3.eq("order_menu_id", orderSetMenu.getId());
                        List<OrderSetMenuGoods> orderSetMenuGoods = orderSetMenuGoodsMapper.selectList(queryWrapper3);//套餐详情的集合


                        List<SelectUserOrderMenuGoodsDto> orderMenuGoodsDtos = new ArrayList<>();//返回套餐商品集合
                        for (OrderSetMenuGoods orderSetMenuGood : orderSetMenuGoods) {
                            SelectUserOrderMenuGoodsDto orderMenuGoodsDto = new SelectUserOrderMenuGoodsDto();
                            BeanUtils.copyProperties(orderSetMenuGood, orderMenuGoodsDto);

                            orderMenuGoodsDtos.add(orderMenuGoodsDto);
                        }

                        SelectUserOrderMenuDto userOrderMenuDto = new SelectUserOrderMenuDto();
                        BeanUtils.copyProperties(orderSetMenu, userOrderMenuDto);
                        userOrderMenuDto.setOrderMenuGoodsDtos(new ArrayList<>());//开辟空间
                        userOrderMenuDto.getOrderMenuGoodsDtos().addAll(orderMenuGoodsDtos);//给套餐中添加商品详细的集合
                        orderMenuDtos.add(userOrderMenuDto);

                    }
                }
            }

            userOrderDTO.setOrderServiceDtos(new ArrayList<>());//开辟空间
            userOrderDTO.getOrderServiceDtos().addAll(serviceDtos);//添加服务集合
            userOrderDTO.setOrderGoodsDtos(new ArrayList<>());//开辟空间
            userOrderDTO.getOrderGoodsDtos().addAll(OrderGoodsDtos);//添加商品集合
            userOrderDTO.setOrderMenuDtos(new ArrayList<>());//开辟空间
            userOrderDTO.getOrderMenuDtos().addAll(orderMenuDtos);//添加套餐集合
            listDto.add(userOrderDTO);
        }
        return listDto;
    }


    //商家根据转态查询订单
    @Override
    public List<SelectShopOrderDto> selectShopOrder(SelectShopOrderParam param) {
        List<SelectShopOrderDto> listDto = new ArrayList<>();//返回对象

        QueryWrapper<NewOrder> queryWrapper = new QueryWrapper<>();

        Integer status = param.getStatus();//(0全部（预约已经成功，已经支付）,1超时警告,2售后管理,3已完成,4新的预约，5预约完成，6预约失败))
        if (status == 0) {//全部
            queryWrapper.eq("shop_id", param.getShopId())//商家id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    .eq("pay_status", 1)
                    .or()
                    .eq("shop_id", param.getShopId())//商家id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    .eq("pay_status", 2)
                    .or()
                    .eq("shop_id", param.getShopId())//商家id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    .eq("pay_status", 3)
                    .or()
                    .eq("shop_id", param.getShopId())//商家id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    .eq("pay_status", 4);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
        }
        if (status == 1) {//超时警告
            LocalDateTime now = LocalDateTime.now();
            queryWrapper.eq("shop_id", param.getShopId());//商家id
            queryWrapper.le("start_time", now);//开始时间小于当前时间
            queryWrapper.ge("ent_time", now);//结束时间大于当前时间
            queryWrapper.eq("pay_status", 1);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            queryWrapper.eq("server_code_status", 0);//验卷状态0未验卷，1验卷成功
        }

        if (status == 2) {//售后管理
            queryWrapper
                    .eq("shop_id", param.getShopId())//商家id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    .eq("pay_status", 2)
                    .or()
                    .eq("shop_id", param.getShopId())//商家id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    .eq("pay_status", 3)
                    .or()
                    .eq("shop_id", param.getShopId())//商家id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    .eq("pay_status", 4);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
        }
        if (status == 3) {//3已完成
            queryWrapper.eq("shop_id", param.getShopId());//商家id
            queryWrapper.eq("appointment_status", 1);//预约状态（0预约中，1预约成功，2预约失败）
            queryWrapper.eq("pay_status", 1);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            queryWrapper.eq("server_code_status", 1);//验卷状态0未验卷，1验卷成功
        }

        if (status == 4) {//预约中
            queryWrapper.eq("shop_id", param.getShopId());//商家id
            queryWrapper.eq("appointment_status", 0);
        }
        if (status == 5) {//预约成功
            queryWrapper.eq("shop_id", param.getShopId());//商家id
            queryWrapper.eq("appointment_status", 1);
        }
        if (status == 6) {//预约失败
            queryWrapper.eq("shop_id", param.getShopId());//商家id
            queryWrapper.eq("appointment_status", 2);
        }


        List<NewOrder> newOrders = newOrderMapper.selectList(queryWrapper);//根据状态查询用户的所有订单


        for (NewOrder newOrder : newOrders) {
            SelectShopOrderDto shopOrderDto = new SelectShopOrderDto();//单个订单返回对象
            BeanUtils.copyProperties(newOrder, shopOrderDto);
            List<SelectUserOrderServiceDto> serviceDtos = new ArrayList<>();
            List<SelectUserOrderGoodsDto> OrderGoodsDtos = new ArrayList<>();
            List<SelectUserOrderMenuDto> orderMenuDtos = new ArrayList<>();


            if (newOrder.getOrderType() == 0) {//（0-服务类(只有服务)
                QueryWrapper<OrderService> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("order_id", newOrder.getId());
                List<OrderService> orderServices = orderServiceMapper.selectList(queryWrapper1);//查服务订单详情
                for (OrderService orderService : orderServices) {
                    SelectUserOrderServiceDto serviceDto = new SelectUserOrderServiceDto();
                    BeanUtils.copyProperties(orderService, serviceDto);
                    serviceDtos.add(serviceDto);
                }

            }


            if (newOrder.getOrderType() == 1) {//1-普通类（套餐，单品集合）

                QueryWrapper<OrderGoods> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("order_id", newOrder.getId());
                List<OrderGoods> orderGoods = orderGoodsMapper.selectList(queryWrapper1);//商品的集合
                for (OrderGoods orderGood : orderGoods) {
                    SelectUserOrderGoodsDto orderGoodsDto = new SelectUserOrderGoodsDto();
                    BeanUtils.copyProperties(orderGood, orderGoodsDto);
                    OrderGoodsDtos.add(orderGoodsDto);
                }


                QueryWrapper<OrderSetMenu> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("order_id", newOrder.getId());
                List<OrderSetMenu> orderSetMenus = orderSetMenuMapper.selectList(queryWrapper2);//套餐集合

                if (orderSetMenus.size() > 0) {

                    for (OrderSetMenu orderSetMenu : orderSetMenus) {
                        QueryWrapper<OrderSetMenuGoods> queryWrapper3 = new QueryWrapper<>();
                        queryWrapper3.eq("order_menu_id", orderSetMenu.getId());
                        List<OrderSetMenuGoods> orderSetMenuGoods = orderSetMenuGoodsMapper.selectList(queryWrapper3);//套餐详情的集合


                        List<SelectUserOrderMenuGoodsDto> orderMenuGoodsDtos = new ArrayList<>();//返回套餐商品集合
                        for (OrderSetMenuGoods orderSetMenuGood : orderSetMenuGoods) {
                            SelectUserOrderMenuGoodsDto orderMenuGoodsDto = new SelectUserOrderMenuGoodsDto();
                            BeanUtils.copyProperties(orderSetMenuGood, orderMenuGoodsDto);

                            orderMenuGoodsDtos.add(orderMenuGoodsDto);
                        }

                        SelectUserOrderMenuDto userOrderMenuDto = new SelectUserOrderMenuDto();
                        BeanUtils.copyProperties(orderSetMenu, userOrderMenuDto);
                        userOrderMenuDto.setOrderMenuGoodsDtos(new ArrayList<>());//开辟空间
                        userOrderMenuDto.getOrderMenuGoodsDtos().addAll(orderMenuGoodsDtos);//给套餐中添加商品详细的集合
                        orderMenuDtos.add(userOrderMenuDto);

                    }
                }
            }

            shopOrderDto.setOrderServiceDtos(new ArrayList<>());//开辟空间
            shopOrderDto.getOrderServiceDtos().addAll(serviceDtos);//添加服务集合
            shopOrderDto.setOrderGoodsDtos(new ArrayList<>());//开辟空间
            shopOrderDto.getOrderGoodsDtos().addAll(OrderGoodsDtos);//添加商品集合
            shopOrderDto.setOrderMenuDtos(new ArrayList<>());//开辟空间
            shopOrderDto.getOrderMenuDtos().addAll(orderMenuDtos);//添加套餐集合
            listDto.add(shopOrderDto);
        }


        return listDto;
    }


    //用户删除订单
    @Override
    @Transactional
    public boolean deletedUserOrder(Long orderId) {

        try {
            NewOrder newOrder = newOrderMapper.selectById(orderId);
            Integer orderType = newOrder.getOrderType();//订单类型（0-服务类(只有服务)，1-普通类（套餐，单品集合））
            Integer payStatus = newOrder.getPayStatus();
            Integer serverCodeStatus = newOrder.getServerCodeStatus();
            if (payStatus == 1 && serverCodeStatus == 0) {
                throw new JSYException(500, "未消费订单不能删除");
            }

            //删除商品
            QueryWrapper<OrderGoods> wrapper2 = new QueryWrapper<>();
            wrapper2.eq("order_id", orderId);
            int delete2 = orderGoodsMapper.delete(wrapper2);

            if (orderType == 0) { //删除订单服务
                QueryWrapper<OrderService> wrapper1 = new QueryWrapper<>();
                wrapper1.eq("order_id", orderId);
                int delete1 = orderServiceMapper.delete(wrapper1);
            }


            if (orderType == 1) {  //删除套餐
                QueryWrapper<OrderSetMenu> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("order_id", orderId);
                List<OrderSetMenu> orderSetMenus = orderSetMenuMapper.selectList(queryWrapper);
                orderSetMenus.forEach(s -> {
                    Long id = s.getId();
                    QueryWrapper<OrderSetMenuGoods> wrapper3 = new QueryWrapper<>();
                    wrapper3.eq("order_menu_id", id);
                    int delete3 = orderSetMenuGoodsMapper.delete(wrapper3);
                });

                QueryWrapper<OrderSetMenu> wrapper4 = new QueryWrapper<>();
                wrapper4.eq("order_id", orderId);
                int delete4 = orderSetMenuMapper.delete(wrapper4);
            }


            //删除订单
            QueryWrapper<NewOrder> wrapper = new QueryWrapper<>();
            wrapper.eq("id", orderId);
            int delete = newOrderMapper.delete(wrapper);


        } catch (Exception e) {
            throw new JSYException(500, e.getMessage());
        }

        return true;
    }

    //商家同意预约订单
    @Override
    public Boolean consentOrder(Long shopId, Long orderId) {
        NewOrder newOrder = new NewOrder();
        newOrder.setShopId(shopId);
        newOrder.setAppointmentStatus(1);
        newOrder.setId(orderId);
        int update = newOrderMapper.updateById(newOrder);
//        if (update > 0) {
//            return true;
//        }
        return true;
    }


    //用户支付后调用的接口（回调）
    @Override
    public Boolean completionPay(CompletionPayParam param) {
        NewOrder newOrder = new NewOrder();
//        newOrder.setId(param.getId());//订单id
        newOrder.setPayStatus(1);//支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）
        newOrder.setBillMonth(param.getBillMonth());//账单月份
        newOrder.setBillNum(param.getBillNum());//账单号
        newOrder.setBillRise(param.getBillRise());//账单抬头
        Long payTime = param.getPayTime();
        LocalDateTime localDateTime = new Date(payTime).toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
        newOrder.setPayTime(localDateTime);//支付时间
        newOrder.setPayType(param.getPayType());//支付方式
        newOrder.setServeCode(Random8.getNumber8());//生成验劵码转成大写8位
        UpdateWrapper<NewOrder> updateWrapper = new UpdateWrapper<>();
        System.out.println("回调回来order_num" + param.getOrderNumber());
        updateWrapper.eq("order_num", param.getOrderNumber());

        QueryWrapper<NewOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_num", param.getOrderNumber());
        NewOrder newOrder1 = newOrderMapper.selectOne(queryWrapper);//查询订单
        SelectShopOrderDto shopOrderDto = selectOrderByOrderId(newOrder1.getId());//根据id查询订单详情
        HashMap<String, Object> orderData = createOrderData(shopOrderDto);//整理数据
        newOrder.setBillRise(orderData.toString());//填入账单抬头


        int update = newOrderMapper.update(newOrder, updateWrapper);
        if (update > 0) {
            return true;
        }
        return false;
    }

    //支付回调接口修改版
    @Override
    public Boolean replyPayOne(R<PayCallNotice> param) {

//        PayCallNotice data = param.getData();
//        String sysOrderNo = data.getSysOrderNo();//钱包系统订单号
//        basePayRpcService.receiveCall(sysOrderNo);//确认订单回复
//        return true;

        log.info(param.getData().toString());
        PayCallNotice data = param.getData();
        BigDecimal orderAmount = data.getOrderAmount();//支付金额
        LocalDateTime payTime = data.getPayTime();//支付时间
        String sysOrderNo = data.getSysOrderNo();//钱包系统订单号
        String busOrderNo = data.getBusOrderNo();//订单号id
        Integer payType = data.getPayType();//支付类型常量：支付方式 0待定 1支付宝 2微信 3银行卡 4余额
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;//当前月份
        if (data.getPayStatus() != 1) {
            throw new JSYException(500, data.getPayStatus().toString());
        }

        if (data.getPayStatus() == 1) { //0、待支付，1、支付成功，-1、支付失败
            String busOrderNo1 = busOrderNo;
            Long orderId = Long.valueOf(busOrderNo1);
            NewOrder newOrder = new NewOrder();
            newOrder.setId(orderId);
            newOrder.setPayType(payType);//支付方式 1支付宝 2微信
            newOrder.setPayStatus(1);//支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）
            newOrder.setBillMonth(month);//账单月份
            newOrder.setBillNum(sysOrderNo);//账单号
            newOrder.setPayTime(payTime);//支付时间
            newOrder.setServeCode(Random8.getNumber8());//生成验劵码转成大写11位
            SelectShopOrderDto shopOrderDto = selectOrderByOrderId(orderId);//根据id查询订单详情
            HashMap<String, Object> orderData = createOrderData(shopOrderDto);//整理数据
            newOrder.setBillRise(orderData.toString());//填入账单抬头

            int update = newOrderMapper.updateById(newOrder);
            if (update > 0) {
                basePayRpcService.receiveCall(sysOrderNo);//确认订单回复
                log.info("确认订单回复完成");
                return true;
            }

        }
        return false;
    }

    //商家验卷接口
    @Override
    public boolean acceptanceCheck(Long shopId, String code, Long orderId) {
        QueryWrapper<NewOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", shopId);
        queryWrapper.eq("order_status", 1);//未完成订单
        queryWrapper.eq("pay_status", 1);//确保已经支付
        queryWrapper.eq("appointment_status", 1);//确保预约成功
        queryWrapper.eq("serve_code", code);//验收码
        queryWrapper.eq("id", orderId);//订单id
        NewOrder newOrder = newOrderMapper.selectOne(queryWrapper);
        if (newOrder == null) {
            throw new JSYException(500, "没有此订单");

        }
        if (newOrder != null) {
            newOrder.setServerCodeStatus(1);//验卷变为已验卷
            int i = newOrderMapper.updateById(newOrder);
            if (i > 0) {
                return true;
            }
        }

        return false;
    }

    //商家根据验证码查询订单
    @Override
    public SelectShopOrderDto shopConsentOrder(ShopConsentOrderParam param) {


        QueryWrapper<NewOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", param.getShopId());//商家id
        queryWrapper.eq("serve_code", param.getCode());//验证码
        queryWrapper.eq("appointment_status", 1);//预约状态(0预约中，1预约成功，2预约失败）
        queryWrapper.eq("pay_status", 1);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
        NewOrder newOrder = newOrderMapper.selectOne(queryWrapper);//根据状态查询用户的所有订单
        if (newOrder == null) {
            throw new JSYException(10002, "无验证消息");
        }

        SelectShopOrderDto shopOrderDto = new SelectShopOrderDto();//单个订单返回对象
        BeanUtils.copyProperties(newOrder, shopOrderDto);
        List<SelectUserOrderServiceDto> serviceDtos = new ArrayList<>();
        List<SelectUserOrderGoodsDto> OrderGoodsDtos = new ArrayList<>();
        List<SelectUserOrderMenuDto> orderMenuDtos = new ArrayList<>();


        if (newOrder.getOrderType() == 0) {//（0-服务类(只有服务)
            QueryWrapper<OrderService> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("order_id", newOrder.getId());
            List<OrderService> orderServices = orderServiceMapper.selectList(queryWrapper1);//查服务订单详情
            for (OrderService orderService : orderServices) {
                SelectUserOrderServiceDto serviceDto = new SelectUserOrderServiceDto();
                BeanUtils.copyProperties(orderService, serviceDto);
                serviceDtos.add(serviceDto);
            }

        }


        if (newOrder.getOrderType() == 1) {//1-普通类（套餐，单品集合）

            QueryWrapper<OrderGoods> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("order_id", newOrder.getId());
            List<OrderGoods> orderGoods = orderGoodsMapper.selectList(queryWrapper1);//商品的集合
            for (OrderGoods orderGood : orderGoods) {
                SelectUserOrderGoodsDto orderGoodsDto = new SelectUserOrderGoodsDto();
                BeanUtils.copyProperties(orderGood, orderGoodsDto);
                OrderGoodsDtos.add(orderGoodsDto);
            }


            QueryWrapper<OrderSetMenu> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("order_id", newOrder.getId());
            List<OrderSetMenu> orderSetMenus = orderSetMenuMapper.selectList(queryWrapper2);//套餐集合
            if (orderSetMenus.size() > 0) {

                for (OrderSetMenu orderSetMenu : orderSetMenus) {
                    QueryWrapper<OrderSetMenuGoods> queryWrapper3 = new QueryWrapper<>();
                    queryWrapper3.eq("order_menu_id", orderSetMenu.getId());
                    List<OrderSetMenuGoods> orderSetMenuGoods = orderSetMenuGoodsMapper.selectList(queryWrapper3);//套餐详情的集合


                    List<SelectUserOrderMenuGoodsDto> orderMenuGoodsDtos = new ArrayList<>();//返回套餐商品集合
                    for (OrderSetMenuGoods orderSetMenuGood : orderSetMenuGoods) {
                        SelectUserOrderMenuGoodsDto orderMenuGoodsDto = new SelectUserOrderMenuGoodsDto();
                        BeanUtils.copyProperties(orderSetMenuGood, orderMenuGoodsDto);
                        orderMenuGoodsDtos.add(orderMenuGoodsDto);
                    }

                    SelectUserOrderMenuDto userOrderMenuDto = new SelectUserOrderMenuDto();
                    BeanUtils.copyProperties(orderSetMenu, userOrderMenuDto);
                    userOrderMenuDto.setOrderMenuGoodsDtos(new ArrayList<>());//开辟空间
                    userOrderMenuDto.getOrderMenuGoodsDtos().addAll(orderMenuGoodsDtos);//给套餐中添加商品详细的集合
                    orderMenuDtos.add(userOrderMenuDto);

                }
            }
        }

        shopOrderDto.setOrderServiceDtos(new ArrayList<>());//开辟空间
        shopOrderDto.getOrderServiceDtos().addAll(serviceDtos);//添加服务集合
        shopOrderDto.setOrderGoodsDtos(new ArrayList<>());//开辟空间
        shopOrderDto.getOrderGoodsDtos().addAll(OrderGoodsDtos);//添加商品集合
        shopOrderDto.setOrderMenuDtos(new ArrayList<>());//开辟空间
        shopOrderDto.getOrderMenuDtos().addAll(orderMenuDtos);//添加套餐集合
        return shopOrderDto;
    }

    //根据订单id查询订单详情
    @Override
    public SelectShopOrderDto selectOrderByOrderId(Long orderId) {

        QueryWrapper<NewOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", orderId);//商家id
        NewOrder newOrder = newOrderMapper.selectOne(queryWrapper);//根据状态查询用户的所有订单
        if (newOrder == null) {
            throw new JSYException(10004, "无此订单");
        }

        SelectShopOrderDto shopOrderDto = new SelectShopOrderDto();//单个订单返回对象
        BeanUtils.copyProperties(newOrder, shopOrderDto);
        List<SelectUserOrderServiceDto> serviceDtos = new ArrayList<>();
        List<SelectUserOrderGoodsDto> OrderGoodsDtos = new ArrayList<>();
        List<SelectUserOrderMenuDto> orderMenuDtos = new ArrayList<>();


        if (newOrder.getOrderType() == 0) {//（0-服务类(只有服务)
            QueryWrapper<OrderService> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("order_id", newOrder.getId());
            List<OrderService> orderServices = orderServiceMapper.selectList(queryWrapper1);//查服务订单详情
            for (OrderService orderService : orderServices) {
                SelectUserOrderServiceDto serviceDto = new SelectUserOrderServiceDto();
                BeanUtils.copyProperties(orderService, serviceDto);
                serviceDtos.add(serviceDto);
            }

        }

        QueryWrapper<OrderGoods> queryWrapper6 = new QueryWrapper<>();
        queryWrapper6.eq("order_id", newOrder.getId());
        List<OrderGoods> orderGoods = orderGoodsMapper.selectList(queryWrapper6);//商品的集合
        for (OrderGoods orderGood : orderGoods) {
            SelectUserOrderGoodsDto orderGoodsDto = new SelectUserOrderGoodsDto();
            BeanUtils.copyProperties(orderGood, orderGoodsDto);
            OrderGoodsDtos.add(orderGoodsDto);
        }


        if (newOrder.getOrderType() == 1) {//1-普通类（套餐，单品集合）


            QueryWrapper<OrderSetMenu> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("order_id", newOrder.getId());
            List<OrderSetMenu> orderSetMenus = orderSetMenuMapper.selectList(queryWrapper2);//套餐集合
            if (orderSetMenus.size() > 0) {

                for (OrderSetMenu orderSetMenu : orderSetMenus) {
                    QueryWrapper<OrderSetMenuGoods> queryWrapper3 = new QueryWrapper<>();
                    queryWrapper3.eq("order_menu_id", orderSetMenu.getId());
                    List<OrderSetMenuGoods> orderSetMenuGoods = orderSetMenuGoodsMapper.selectList(queryWrapper3);//套餐详情的集合
                    List<SelectUserOrderMenuGoodsDto> orderMenuGoodsDtos = new ArrayList<>();//返回套餐商品集合
                    for (OrderSetMenuGoods orderSetMenuGood : orderSetMenuGoods) {
                        SelectUserOrderMenuGoodsDto orderMenuGoodsDto = new SelectUserOrderMenuGoodsDto();
                        BeanUtils.copyProperties(orderSetMenuGood, orderMenuGoodsDto);
                        orderMenuGoodsDtos.add(orderMenuGoodsDto);
                    }

                    SelectUserOrderMenuDto userOrderMenuDto = new SelectUserOrderMenuDto();
                    BeanUtils.copyProperties(orderSetMenu, userOrderMenuDto);
                    userOrderMenuDto.setOrderMenuGoodsDtos(new ArrayList<>());//开辟空间
                    userOrderMenuDto.getOrderMenuGoodsDtos().addAll(orderMenuGoodsDtos);//给套餐中添加商品详细的集合
                    orderMenuDtos.add(userOrderMenuDto);

                }
            }
        }

        shopOrderDto.setOrderServiceDtos(new ArrayList<>());//开辟空间
        shopOrderDto.getOrderServiceDtos().addAll(serviceDtos);//添加服务集合
        shopOrderDto.setOrderGoodsDtos(new ArrayList<>());//开辟空间
        shopOrderDto.getOrderGoodsDtos().addAll(OrderGoodsDtos);//添加商品集合
        shopOrderDto.setOrderMenuDtos(new ArrayList<>());//开辟空间
        shopOrderDto.getOrderMenuDtos().addAll(orderMenuDtos);//添加套餐集合
        return shopOrderDto;
    }

    //支付宝支付接口
    @Override
    @Transactional
    public CommonResult alipay(Long orderId) {
        log.info("支付宝支付url" + urlAliPay);

        NewOrder newOrder = newOrderMapper.selectById(orderId);

        if (newOrder == null) {
            throw new JSYException(500, "无此订单");
        }

        Integer payStatus = newOrder.getPayStatus();
        if (payStatus == 1 || payStatus == 2 || payStatus == 3 || payStatus == 4) {
            throw new JSYException(500, "已支付");
        }
        String token = ContextHolder.getContext().getLoginUser().getToken();//获取用户token
        SelectShopOrderDto shopOrderDto = selectOrderByOrderId(orderId);
        //String orderNum = shopOrderDto.getOrderNum();//订单编号
        BigDecimal orderAllPrice = shopOrderDto.getOrderAllPrice();//订单的最终价格
        HashMap<String, Object> orderData = createOrderData(shopOrderDto);
        System.out.println(orderData);
        AliAppPayQO aliAppPayQO = new AliAppPayQO();//支付的参数对象
        aliAppPayQO.setCommunityId(1L);//社区id
        aliAppPayQO.setServiceOrderNo(newOrder.getId().toString());//订单id
        // aliAppPayQO.setOutTradeNo(orderNum);//系统订单号
        // aliAppPayQO.setPayType(1);//支付类型 1.APP 2.H5
        aliAppPayQO.setTotalAmount(new BigDecimal(0.01));//交易金额(RMB)
        aliAppPayQO.setTradeFrom(2);//交易来源 1.充值提现2.商城购物3.水电缴费4.物业管理5.房屋租金6.红包7.红包退回.8停车缴费.9房屋租赁.10临时车辆缴费
        aliAppPayQO.setOrderData(orderData);//订单详情
        aliAppPayQO.setBody(orderData.toString());//订单附加信息
        aliAppPayQO.setReceiveUid(newOrder.getShopId());//收款方id
        String urlParam = urlAliPay;//支付宝支付url
        CommonResult commonResult = HttpClientHelper.sendPost(urlParam, aliAppPayQO, token, CommonResult.class);
        log.info(commonResult.toString() + "*********");
        AliPayVO vo = JSON.parseObject(commonResult.getData().toString(), AliPayVO.class);

        System.out.println("支付时的订单号" + vo.getOrderNum());
        NewOrder entity = new NewOrder();
        entity.setId(orderId);
        entity.setOrderNum(vo.getOrderNum());
        int i = newOrderMapper.updateById(entity);
        if (i > 0) {
            log.info("订单号修改成功");
        }
        if (i < 1) {
            throw new JSYException(500, "系统繁忙");
        }

        return commonResult;
    }

    //微信支付接口
    @Override
    @Transactional
    public CommonResult WeChatPay(Long orderId) {
        log.info("微信支付url" + urlWeChatPay);
        NewOrder newOrder = newOrderMapper.selectById(orderId);
        Integer payStatus = newOrder.getPayStatus();
        if (payStatus == 1 || payStatus == 2 || payStatus == 3 || payStatus == 4) {
            throw new JSYException(500, "已支付");
        }
        String token = ContextHolder.getContext().getLoginUser().getToken();//获取用户token
        SelectShopOrderDto shopOrderDto = selectOrderByOrderId(orderId);
        String orderNum = shopOrderDto.getOrderNum();//订单编号
        BigDecimal orderAllPrice = shopOrderDto.getOrderAllPrice();//订单的最终价格
        HashMap<String, Object> orderData = createOrderData(shopOrderDto);
        WeChatPayQO weChatPayQO = new WeChatPayQO();
        weChatPayQO.setTradeFrom(2);//商城购物2
        weChatPayQO.setAmount(new BigDecimal(0.01));
        weChatPayQO.setOrderData(orderData);
        weChatPayQO.setReceiveUid(newOrder.getShopId());//收款方id
        weChatPayQO.setServiceOrderNo(newOrder.getId().toString());
        System.out.println(orderData.toString());
        //weChatPayQO.setDescriptionStr(orderData.toString().length()>30?"商城购物":orderData.toString());//支付描述
        weChatPayQO.setDescriptionStr("商城购物");//支付描述
        String urlParam = urlWeChatPay;//微信支付的url
        CommonResult commonResult = HttpClientHelper.sendPost(urlParam, weChatPayQO, token, CommonResult.class);
        System.out.println(commonResult.toString());
        WeChatPayVO vo = JSON.parseObject(commonResult.getData().toString(), WeChatPayVO.class);
        System.out.println(vo.getOrderNum());
        NewOrder entity = new NewOrder();
        entity.setId(orderId);
        entity.setOrderNum(vo.getOrderNum());
        int i = newOrderMapper.updateById(entity);
        if (i < 1) {
            throw new JSYException(500, "系统繁忙");
        }
        return commonResult;
    }

    //退款接口
    @Override
    public Boolean allPayRefund(Long orderId) {

        NewOrder newOrder = newOrderMapper.selectById(orderId);
        if (newOrder == null) {
            throw new JSYException(500, "无此订单");
        }
        String orderNum = newOrder.getOrderNum();//商户订单号
        String token = ContextHolder.getContext().getLoginUser().getToken();//获取用户token

        Integer payStatus = newOrder.getPayStatus();
        if (payStatus==3) {
            throw new JSYException(200,"已退款");
        }

        //（ 1app支付宝，2app微信)
        if (newOrder.getPayType() == 1) {
            log.info("支付宝退款url" + urlAliRefund);
            AlipayRefundParam param = new AlipayRefundParam();
            param.setOrderNo(newOrder.getOrderNum());//订单号
            log.info("订单号" + param);
            CommonResult commonResult = HttpClientHelper.sendPost(urlAliRefund, param, token, CommonResult.class);
            log.info("commonResult**********" + commonResult.toString());
            if ((Boolean) commonResult.getData() == false) {
                throw new JSYException(commonResult.getCode(), commonResult.getMessage());
            }
            Boolean vo = JSON.parseObject(commonResult.getData().toString(), Boolean.class);
            if(vo){
                newOrder.setPayStatus(3);//将支付状态改为3（退款成功）
                int i = newOrderMapper.updateById(newOrder);
                if(i>0){
                    return  true;
                }
            }
            return false;
        }
        if (newOrder.getPayType() == 2) {//微信支付的
            log.info("微信退款url" + urlWeChatRefund);
            log.info("查询微信退款状态url" + urlWeChatRefundStatus);
            WechatRefundQO wechatRefundQO = new WechatRefundQO();
            wechatRefundQO.setCommunityId(1L);
            wechatRefundQO.setOrderNum(orderNum);
            wechatRefundQO.setTradeFrom(2);//2.商城购物
            wechatRefundQO.setServiceOrderNo(String.valueOf(orderId));//订单id
            CommonResult commonResult = HttpClientHelper.sendPost(urlWeChatRefund, wechatRefundQO, token, CommonResult.class);
            log.info("commonResult**********退款" + commonResult.toString());
            if (commonResult.getCode() != 0) {
                throw new JSYException(commonResult.getCode(), commonResult.getMessage());
            }
//            String urlParamReturn = urlWeChatRefundStatus + orderId;//查询退款状态
//            log.info("urlParamReturn**********************"+urlParamReturn);
//            CommonResult commonResult1 = HttpClientHelper.sendGet(urlParamReturn, token, CommonResult.class);
//            log.info("commonResult**********退款状态" + commonResult.toString());
//            log.info(commonResult1.getData()+"****值");
//            log.info(commonResult1.getData().toString()+"****toString");
            Boolean vo = JSON.parseObject(commonResult.getData().toString(), Boolean.class);
            log.info("Boolean***************"+vo.toString());
            if (vo) {
                newOrder.setPayStatus(3);//将支付状态改为3,退款成功
                int i = newOrderMapper.updateById(newOrder);
                if (i > 0) {
                    log.info("退款转态修改成功");
                    return true;
                }

            }
            return false;
        }
        return false;
    }


    //根据查询的数据支付组合订单详情
    public HashMap<String, Object> createOrderData(SelectShopOrderDto shopOrderDto) {
        HashMap<String, Object> orderData = new HashMap<>();
        Integer orderType1 = shopOrderDto.getOrderType();//订单类型（0-服务类(只有服务)，1-普通类（套餐，单品集合））
        if (orderType1 == 0) {//服务类
            List<SelectUserOrderServiceDto> orderServiceDtos = shopOrderDto.getOrderServiceDtos();//服务详情
            System.out.println("服务" + orderServiceDtos.size());
            if (orderServiceDtos != null && orderServiceDtos.size() > 0) {//服务详情有数据的时候
                for (SelectUserOrderServiceDto value : orderServiceDtos) {
                    orderData.put(value.getTitle(),
                            "数量" + value.getAmount() +
                                    ";单价" + (value.getDiscountState() == 1 ? value.getDiscountPrice() : value.getPrice()));
                }
            }
            List<SelectUserOrderGoodsDto> orderGoodsDtos = shopOrderDto.getOrderGoodsDtos();//商品详情
            System.out.println("商品" + orderGoodsDtos.size());
            if (orderGoodsDtos != null && orderGoodsDtos.size() > 0) {//商品详情有数据的时候

                orderGoodsDtos.forEach(value -> {
                    orderData.put(value.getTitle(),
                            "数量" + value.getAmount() +
                                    ";单价" + (value.getDiscountState() == 1 ? value.getDiscountPrice() : value.getPrice()));
                });

            }
        }

        if (orderType1 == 1) {//普通类
            List<SelectUserOrderMenuDto> orderMenuDtos = shopOrderDto.getOrderMenuDtos();//套餐详情
            System.out.println("套餐" + orderMenuDtos.size());
            if (orderMenuDtos != null && orderMenuDtos.size() > 0) {//套餐详情有数据的时候
                for (SelectUserOrderMenuDto value : orderMenuDtos) {
                    String details = "";
                    List<SelectUserOrderMenuGoodsDto> orderMenuGoodsDtos = value.getOrderMenuGoodsDtos();
                    for (SelectUserOrderMenuGoodsDto a : orderMenuGoodsDtos) {
                        details = details + a.getTitle() + ":数量" + a.getAmount() + ";";
                    }
                    details.substring(0, details.length() - 2);
                    orderData.put(value.getName(), "数量" + value.getAmount() +
                            ";单价" + (value.getDiscountState() == 1 ? value.getSellingPrice() : value.getRealPrice()) + ";套餐详情【" + details + "】");
                }
            }

            List<SelectUserOrderGoodsDto> orderGoodsDtos = shopOrderDto.getOrderGoodsDtos();//商品详情
            System.out.println("商品" + orderGoodsDtos.size());
            if (orderGoodsDtos != null && orderGoodsDtos.size() > 0) {//商品详情有数据的时候

                orderGoodsDtos.forEach(value -> {
                    orderData.put(value.getTitle(), "数量" + value.getAmount() +
                            ";单价" + (value.getDiscountState() == 1 ? value.getDiscountPrice() : value.getPrice()));
                });
            }
        }
        return orderData;
    }


    //查询近多少日订单量
    @Override
    public OrderSizeDto orderSize(OrderSizeParam param) {

        OrderSizeDto orderSizeDto = new OrderSizeDto();

        Integer number = param.getNumber();//几天
        LocalDateTime end = LocalDateTime.now();//当前时间


        LocalDateTime start = LocalDate.now().plusDays(-(number - 1)).atStartOfDay();
        System.out.println(start);
        System.out.println(end);
        QueryWrapper<NewOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", param.getShopId());
        queryWrapper.ge("pay_time", start);//大于等于前几天的时间
        queryWrapper.le("pay_time", end);//小于等于当前时间
        List<NewOrder> newOrders = newOrderMapper.selectList(queryWrapper);
        orderSizeDto.setAmount(newOrders.size());
        orderSizeDto.setList(new ArrayList<>());
        for (Integer i = number; i > 0; i--) {
            OrderSizeDayDto orderSizeDayDto = new OrderSizeDayDto();//返回对象


            LocalDateTime start1 = LocalDate.now().plusDays(-(i - 1)).atStartOfDay();
            LocalDateTime end1 = LocalDate.now().plusDays(-(i - 2)).atStartOfDay();
            System.out.println("_________________________" + start1 + "**" + end1);

            QueryWrapper<NewOrder> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("shop_id", param.getShopId());
            queryWrapper1.ge("pay_time", start1);//大于等于前几天的时间
            queryWrapper1.lt("pay_time", end1);//小于(start1的第二天开始时间）
            List<NewOrder> newOrders1 = newOrderMapper.selectList(queryWrapper1);
            orderSizeDayDto.setTime(LocalDate.now().plusDays(-(i - 1)));
            orderSizeDayDto.setNumber(newOrders1.size());
            orderSizeDto.getList().add(orderSizeDayDto);

        }
        return orderSizeDto;
    }

    //修改生成订单商品数量（单个商品直接购买）
    @Override
    public Boolean updateOrderOne(UpdateOrderOneParam param) {

        Integer typeByGoods = param.getTypeByGoods(); //"0商品1服务2套餐"
        if (typeByGoods == 0) {
            OrderGoods entity = new OrderGoods();
            entity.setAmount(param.getNumber());
            UpdateWrapper<OrderGoods> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("order_id", param.getOrderId());
            updateWrapper.eq("goods_id", param.getCommodityId());
            int update = orderGoodsMapper.update(entity, updateWrapper);
            if (update > 0) {
                return true;
            }
        } else if (typeByGoods == 1) {
            OrderService entity = new OrderService();
            entity.setAmount(param.getNumber());
            UpdateWrapper<OrderService> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("order_id", param.getOrderId());
            updateWrapper.eq("service_id", param.getCommodityId());
            int update = orderServiceMapper.update(entity, updateWrapper);
            if (update > 0) {
                return true;
            }
        } else if (typeByGoods == 2) {
            OrderSetMenu entity = new OrderSetMenu();
            entity.setAmount(param.getNumber());
            UpdateWrapper<OrderSetMenu> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("order_id", param.getOrderId());
            updateWrapper.eq("menu_id", param.getCommodityId());
            int update = orderSetMenuMapper.update(entity, updateWrapper);
            if (update > 0) {
                return true;
            }
        }


        return false;
    }
    //查询相应状态下的数量
    @Override
    public ArrayList<SelectUserOrderNumberDto> selectUserOrderNumber(Long id) {
        ArrayList<SelectUserOrderNumberDto> selectUserOrderNumberDtos = new ArrayList<SelectUserOrderNumberDto>();
        //状态1代付款2待消费3已完成4售后

            //1代付款
            QueryWrapper<NewOrder> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", id);//用户id
            queryWrapper.eq("order_status", 1);//订单状态（[1待上门、待配送、待发货]，2、完成）
            queryWrapper.eq("pay_status", 0);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            Integer number = newOrderMapper.selectCount(queryWrapper);
            SelectUserOrderNumberDto selectUserOrderNumberDto = new SelectUserOrderNumberDto(1,number);


        //2待消费
        QueryWrapper<NewOrder> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("user_id", id);//用户id
            queryWrapper1.eq("appointment_status", 1);//预约状态（0预约中，1预约成功，2预约失败）
            queryWrapper1.eq("pay_status", 1);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            queryWrapper1.eq("server_code_status", 0);//验卷状态0未验卷，1验卷成功
        Integer number1 = newOrderMapper.selectCount(queryWrapper1);
        SelectUserOrderNumberDto selectUserOrderNumberDto1 = new SelectUserOrderNumberDto(2,number1);

        //3针对页面上的已完成（已经使用了，验过卷）
        QueryWrapper<NewOrder> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("user_id", id);//用户id
            queryWrapper2.eq("appointment_status", 1);//预约状态（0预约中，1预约成功，2预约失败）
            queryWrapper2.eq("pay_status", 1);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            queryWrapper2.eq("server_code_status", 1);//验卷状态0未验卷，1验卷成功
        Integer number2 = newOrderMapper.selectCount(queryWrapper2);
        SelectUserOrderNumberDto selectUserOrderNumberDto2 = new SelectUserOrderNumberDto(3,number2);


        //4退款/售后
           QueryWrapper<NewOrder> queryWrapper3 = new QueryWrapper<>();
            queryWrapper3
                    .eq("user_id", id)//用户id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    .eq("pay_status", 2)//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
                    .or()
                    .eq("user_id", id)//用户id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    .eq("pay_status", 3)//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
                    .or()
                    .eq("user_id", id)//用户id
                    .eq("appointment_status", 1)//预约状态（0预约中，1预约成功，2预约失败）
                    .eq("pay_status", 4);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
        Integer number3 = newOrderMapper.selectCount(queryWrapper3);
        SelectUserOrderNumberDto selectUserOrderNumberDto3 = new SelectUserOrderNumberDto(4,number3);

        selectUserOrderNumberDtos.add(selectUserOrderNumberDto);
        selectUserOrderNumberDtos.add(selectUserOrderNumberDto1);
        selectUserOrderNumberDtos.add(selectUserOrderNumberDto2);
        selectUserOrderNumberDtos.add(selectUserOrderNumberDto3);

        return selectUserOrderNumberDtos;
    }
    //大后台查询多有订单
    @Override
    public List<SelectAllOrderByBackstageDto> selectAllOrderByBackstage(SelectAllOrderByBackstageParam param) {
        SelectAllOrderMapperParam selectAllOrderMapperParam = new SelectAllOrderMapperParam();
        Integer page = param.getPage();
        Integer size = param.getSize();
        selectAllOrderMapperParam.setStart((page-1)*size);
        selectAllOrderMapperParam.setEnd(size);
        selectAllOrderMapperParam.setStartTime(param.getStartTime());
        selectAllOrderMapperParam.setEndTime(param.getEndTime());
        selectAllOrderMapperParam.setStatus(param.getStatus());
        List<SelectAllOrderByBackstageVo>  vo= newOrderMapper.selectAllOrderByBackstage(selectAllOrderMapperParam);

        List<SelectAllOrderByBackstageDto> dtoList=new ArrayList<>();//返回对象

        for (SelectAllOrderByBackstageVo one : vo) {
            SelectAllOrderByBackstageDto  dto=new SelectAllOrderByBackstageDto();
            BeanUtils.copyProperties(one,dto);
            Integer orderStatus = one.getOrderStatus();//订单状态
            Integer appointmentStatus = one.getAppointmentStatus();//预约状态（0预约中，1预约成功）
            Integer commentStatus = one.getCommentStatus();//是否评价0未评价，1评价
            Integer payStatus = one.getPayStatus();//支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）
            Integer serverCodeStatus = one.getServerCodeStatus();//验卷状态0未验卷，1验卷成功
            Integer refundApplyRole = one.getRefundApplyRole();//申请退款角色0商家1平台
            String currentStatus=null;
//  0待消费、1已完成、2商家退款中、3商家已退款、4官方退款中5、官方已退款-->
//<!--   2 商家退款中（支付状态为退款中，预约成功,退款角色为商家）-->
//<!--   3商家已退款 （支付状态为退款成功，预约成功,退款角色为商家）-->
//<!--    4 官方退款中（支付状态为退款中，预约成功,退款角色为官方）-->
//<!--    5 官方已退款（支付状态为退款成功，预约成功,退款角色为官方）-->


           if(orderStatus==1 && appointmentStatus==1 && serverCodeStatus==0 && payStatus==1){
               currentStatus="待消费";
           }else if (appointmentStatus==1 && serverCodeStatus==1 && payStatus==1 && commentStatus==1){
               currentStatus="已完成";
           }else if (orderStatus==1 && appointmentStatus==1  && payStatus==2 && refundApplyRole==0){
               currentStatus="商家退款中";
           }else if (orderStatus==1 && appointmentStatus==1  && payStatus==3 && refundApplyRole==0){
               currentStatus="商家已退款";
           }else if (orderStatus==1 && appointmentStatus==1  && payStatus==2 && refundApplyRole==1){
               currentStatus="官方退款中";
           }else if (orderStatus==1 && appointmentStatus==1  && payStatus==3 && refundApplyRole==1){
               currentStatus="官方已退款";
           }


            dto.setCurrentStatus(currentStatus);//当前状态


            List<SelectAllOrderBelongDto> belongDto=new ArrayList<>();

            List<OrderGoodsVO> orderGoodsList = one.getOrderGoodsList();
            List<OrderServiceVO> orderServiceList = one.getOrderServiceList();
            List<OrderSetMenuVO> orderSetMenuList = one.getOrderSetMenuList();
            if (orderGoodsList!=null && orderGoodsList.size()>0 ) {
                for (OrderGoodsVO goodsVO : orderGoodsList) {
                    SelectAllOrderBelongDto  dto1=new SelectAllOrderBelongDto();
                    dto1.setName(goodsVO.getTitle());
                    dto1.setPictures(goodsVO.getImages());
                    dto1.setPrice(goodsVO.getDiscountState()==0?goodsVO.getPrice():goodsVO.getDiscountPrice());
                    dto1.setNumber(goodsVO.getAmount());
                    belongDto.add(dto1);
                }


            }
            if (orderServiceList!=null && orderServiceList.size()>0 ) {
                for (OrderServiceVO serviceVO : orderServiceList) {
                    SelectAllOrderBelongDto  dto1=new SelectAllOrderBelongDto();
                    dto1.setName(serviceVO.getTitle());
                    dto1.setPictures(serviceVO.getImages());
                    dto1.setPrice(serviceVO.getDiscountState()==0?serviceVO.getPrice():serviceVO.getDiscountPrice());
                    dto1.setNumber(serviceVO.getAmount());
                    belongDto.add(dto1);
                }
            }
            if (orderSetMenuList!=null && orderSetMenuList.size()>0 ) {
                for (OrderSetMenuVO menuVO : orderSetMenuList) {
                    SelectAllOrderBelongDto  dto1=new SelectAllOrderBelongDto();
                    dto1.setName(menuVO.getName());
                    dto1.setPictures(menuVO.getImages());
                    dto1.setPrice(menuVO.getDiscountState()==0?menuVO.getRealPrice():menuVO.getSellingPrice());
                    dto1.setNumber(menuVO.getAmount());
                    belongDto.add(dto1);
                }
            }
            dto.setThings(new ArrayList<>());
            dto.getThings().addAll(belongDto);
            dtoList.add(dto);
        }





   //     log.info(vo.toString());
        return dtoList;
    }

}
