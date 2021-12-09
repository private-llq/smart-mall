package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CodeUtils;
import com.jsy.basic.util.utils.OrderNoUtil;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.GoodsClient;
import com.jsy.client.ServiceCharacteristicsClient;
import com.jsy.client.SetMenuClient;
import com.jsy.client.ShoppingCartClient;
import com.jsy.config.HttpClientHelper;
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
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单表 服务实现类
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
@Service
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
            newOrder.setOrderNum(OrderNoUtil.getOrder());//订单号
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

    //创建订单接口返回订单编号
    @Override
    @Transactional
    public Long insterOrder(InsterOrderParam param) {
        Long shopId = param.getShopId();//店铺id
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        Long userId = loginUser.getId();//获取用户id
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
        if(payPrice!=orderAllPrice){
            throw new JSYException(500,"总计不符");
        }

        if (cartData==null) {
            throw new JSYException(500,"没有购物车信息");
        }
       // BigDecimal sumPrice = cartData.getSumPrice();// 商品原价
        newOrder.setOrderAllPrice(payPrice);//订单的最终价格(购物车的支付价格)
        Integer shopType = cartData.getShopType();// 商店类型 服务行业：1是服务行业  0 套餐行业
        if (shopType==1) {
            newOrder.setOrderType(0);//订单类型（0-服务类(只有服务)
        }else if(shopType==0){
            newOrder.setOrderType(1);//订单类型（1-普通类（套餐，单品集合）
        }

        int insert = newOrderMapper.insert(newOrder);

        Long orderId = newOrder.getId();//订单id


        List<ShoppingCartListDto> cartList = cartData.getCartList();//购物车详情


        //添加订单的详情
        try {
            for (ShoppingCartListDto cartListDto : cartList) {

                if ( cartListDto.getState()==false) {
                    throw  new JSYException(500,"商品处于不正常状态");
                }

                Integer type = cartListDto.getType();//0：商品 1：服务  2：套餐
                Long goodsId = cartListDto.getGoodsId();//获取商品的id

                if(type==0){//商品
                    GoodsDto goodsDto = goodsClient.getGoods(goodsId).getData();////根据服务(商品)id查询商品详情
                    if (goodsDto==null) {
                        throw  new JSYException(500,"无商品详情信息");
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
                if(type==1){//服务

                    GoodsServiceDto data = goodsClient.getGoodsService(goodsId).getData();//根据服务(商品)id查询服务详情
                    if (data==null) {
                        throw  new JSYException(500,"无服务详情信息");
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
                if(type==2){//套餐
                    Long setMenuId = cartListDto.getSetMenuId();//获取套餐的id
                    SetMenu menuData = setMenuClient.getShopIdMenus(setMenuId).getData(); //根据套餐id查询套餐详情
                    if (menuData==null) {
                        throw  new JSYException(500,"无套餐详情信息");
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
            e.printStackTrace();
        } catch (BeansException e) {
            e.printStackTrace();
        }


        int code = shoppingCartClient.clearCart(shoppingCartParam).getCode();//0代表清除成功
        if (code!=0) {
            throw  new JSYException(500,"购物车清除失败");
        }




        return orderId;
    }

    //用户根据转态查询订单
    @Override
    public List<SelectUserOrderDto> selectUserOrder(Long id, SelectUserOrderParam param) {

        List<SelectUserOrderDto> listDto = new ArrayList<>();//返回对象

        QueryWrapper<NewOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id);//用户id
        Integer status = param.getStatus();//(0待受理,1已受理,2待消费,3已完成,4退款中)
        if (status == 0) {//0待受理
            queryWrapper.eq("appointment_status", 0);//预约状态（0预约中，1预约成功，2预约失败）
            queryWrapper.eq("order_status", 1);//订单状态（[1待上门、待配送、待发货]，2、完成）
            queryWrapper.eq("pay_status", 0);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            queryWrapper.eq("server_code_status", 0);//验卷状态0未验卷，1验卷成功
        }
        if (status == 1) {//1已受理
            queryWrapper.eq("appointment_status", 1);//预约状态（0预约中，1预约成功，2预约失败）
            queryWrapper.eq("order_status", 1);//订单状态（[1待上门、待配送、待发货]，2、完成）
            queryWrapper.eq("pay_status", 0);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            queryWrapper.eq("server_code_status", 0);//验卷状态0未验卷，1验卷成功
        }
        if (status == 2) {//2待消费
            queryWrapper.eq("appointment_status", 1);//预约状态（0预约中，1预约成功，2预约失败）
            queryWrapper.eq("pay_status", 1);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            queryWrapper.eq("server_code_status", 0);//验卷状态0未验卷，1验卷成功
        }

        if (status == 3) {//3针对页面上的已完成（已经使用了，验过卷）
            queryWrapper.eq("appointment_status", 1);//预约状态（0预约中，1预约成功，2预约失败）
            queryWrapper.eq("pay_status", 1);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
            queryWrapper.eq("server_code_status", 1);//验卷状态0未验卷，1验卷成功
        }
        if (status == 4) {//4退款/售后
            queryWrapper
                    .eq("pay_status", 2)
                    .or()
                    .eq("pay_status", 3)
                    .or()
                    .eq("pay_status", 4);//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
        }

        List<NewOrder> newOrders = newOrderMapper.selectList(queryWrapper);//根据状态查询用户的所有订单

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


            if (orderType == 0) { //删除订单服务
                QueryWrapper<OrderService> wrapper1 = new QueryWrapper<>();
                wrapper1.eq("order_id", orderId);
                int delete1 = orderServiceMapper.delete(wrapper1);
            }


            if (orderType == 1) {
                //删除商品
                QueryWrapper<OrderGoods> wrapper2 = new QueryWrapper<>();
                wrapper2.eq("order_id", orderId);
                int delete2 = orderGoodsMapper.delete(wrapper2);
                //删除套餐

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
            e.printStackTrace();
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
        if (update > 0) {
            return true;
        }
        return false;
    }


    //用户支付后调用的接口
    @Override
    public Boolean completionPay(CompletionPayParam param) {
        NewOrder newOrder = new NewOrder();
//        newOrder.setId(param.getId());//订单id
        newOrder.setPayStatus(1);//支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）
        newOrder.setBillMonth(param.getBillMonth());//账单月份
        newOrder.setBillNum(param.getBillNum());//账单号
        newOrder.setBillRise(param.getBillRise());//账单抬头
        newOrder.setPayTime(param.getPayTime());//支付时间
        newOrder.setPayType(param.getPayType());//支付方式
        newOrder.setServeCode(Random8.getNumber8());//生成验劵码转成大写8位


        UpdateWrapper<NewOrder> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("order_num",param.getOrderNumber());
        int update = newOrderMapper.update(newOrder, updateWrapper);
        if (update > 0) {
            return true;
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
        queryWrapper.eq("order_id", orderId);//订单id
        NewOrder newOrder = newOrderMapper.selectOne(queryWrapper);
        if (newOrder == null) {
            throw new JSYException(500, "没有此订单");

        }
        if (newOrder != null) {
            //newOrder.setOrderStatus(2);//订单转态变为完成状态
            // newOrder.setCommentStatus(0);//评论变为未评价状态
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

    //支付宝支付接口
    @Override
    public CommonResult alipay(Long orderId) {
        String token = ContextHolder.getContext().getLoginUser().getToken();//获取用户token
        SelectShopOrderDto shopOrderDto = selectOrderByOrderId(orderId);
        String orderNum = shopOrderDto.getOrderNum();//订单编号
        BigDecimal orderAllPrice = shopOrderDto.getOrderAllPrice();//订单的最终价格
        HashMap<String, Object> orderData = createOrderData(shopOrderDto);
        AliAppPayQO aliAppPayQO = new AliAppPayQO();//支付的参数对象
        aliAppPayQO.setCommunityId(1L);//社区id
        aliAppPayQO.setOutTradeNo(orderNum);//系统订单号
        aliAppPayQO.setPayType(1);//支付类型 1.APP 2.H5
        aliAppPayQO.setTotalAmount(new BigDecimal(0.01));//交易金额(RMB)
        aliAppPayQO.setTradeFrom(2);//交易来源 1.充值提现2.商城购物3.水电缴费4.物业管理5.房屋租金6.红包7.红包退回.8停车缴费.9房屋租赁.10临时车辆缴费
        aliAppPayQO.setOrderData(orderData);//订单详情
        String urlParam = "http://192.168.12.49:8090/api/v1/payment/alipay/order";
        CommonResult commonResult = HttpClientHelper.sendPost(urlParam, aliAppPayQO, token, CommonResult.class);
        return commonResult;
    }
    //微信支付接口
    @Override
    public CommonResult WeChatPay(Long orderId) {
        String token = ContextHolder.getContext().getLoginUser().getToken();//获取用户token
        SelectShopOrderDto shopOrderDto = selectOrderByOrderId(orderId);
        String orderNum = shopOrderDto.getOrderNum();//订单编号
        BigDecimal orderAllPrice = shopOrderDto.getOrderAllPrice();//订单的最终价格
        HashMap<String, Object> orderData = createOrderData(shopOrderDto);
        WeChatPayQO weChatPayQO=new WeChatPayQO();
       //weChatPayQO.setServiceOrderNo();其他服务id
        weChatPayQO.setTradeFrom(3);//商城购物3
        weChatPayQO.setAmount(new BigDecimal(0.01));
        weChatPayQO.setOrderData(orderData);
        weChatPayQO.setCommunityId(1L);//社区id
        weChatPayQO.setDescriptionStr("商城购物");//支付描述

        String urlParam = "http://192.168.12.49:8090/api/v1/payment/wxPay";
        CommonResult commonResult = HttpClientHelper.sendPost(urlParam, weChatPayQO, token, CommonResult.class);
        return commonResult;
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
                            ";单价" + (value.getDiscountState() == 1 ?value.getSellingPrice():value.getRealPrice()) + ";套餐详情【" + details + "】");
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
}
