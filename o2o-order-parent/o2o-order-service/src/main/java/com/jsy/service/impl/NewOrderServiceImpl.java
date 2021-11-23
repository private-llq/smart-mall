package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.utils.OrderNoUtil;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.ServiceCharacteristicsClient;
import com.jsy.domain.*;
import com.jsy.dto.*;
import com.jsy.mapper.*;
import com.jsy.query.*;
import com.jsy.service.INewOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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
        NewOrder newOrder = new NewOrder();
        newOrder.setOrderNum(OrderNoUtil.getOrder());//订单号
        if (creationOrderParam.getConsumptionWay()==1) {//消费方式（1商家上门)
            newOrder.setAppointmentStatus(0);//预约状态（0预约中，1预约成功，2预约失败）
        }
        if (creationOrderParam.getConsumptionWay()==0) {//消费方式（1商家上门)
            newOrder.setPayStatus(0);//支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）
        }
        //newOrder.setOrderStatus(1);//订单状态（[1待上门、待配送、待发货]，2、完成）
        //newOrder.setPayStatus(0);//支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）
        //newOrder.setCommentStatus(0);//是否评价0未评价，1评价（评价完成为订单完成）
        BeanUtils.copyProperties(creationOrderParam, newOrder);
        int insert = newOrderMapper.insert(newOrder);

        if (creationOrderParam.getOrderType() == 0) {//服务订单
            List<CreationOrderServiceParam> orderServiceParams = creationOrderParam.getOrderServiceParams();
            for (CreationOrderServiceParam Param : orderServiceParams) {
                OrderService orderService = new OrderService();
                orderService.setOrderId(newOrder.getId());//订单id回填
                BeanUtils.copyProperties(Param, orderService);
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
                    int insert1 = orderSetMenuMapper.insert(orderSetMenu);
                    //套餐详情
                    List<CreationOrderMenuGoodsParam> creationOrderMenuGoodsParams = orderMenuParam.getCreationOrderMenuGoodsParams();
                    for (CreationOrderMenuGoodsParam creationOrderMenuGoodsParam : creationOrderMenuGoodsParams) {
                        OrderSetMenuGoods orderSetMenuGoods = new OrderSetMenuGoods();
                        orderSetMenuGoods.setOrderMenuId(orderSetMenu.getId());//套餐id回填
                        BeanUtils.copyProperties(creationOrderMenuGoodsParam, orderSetMenuGoods);
                        int insert2 = orderSetMenuGoodsMapper.insert(orderSetMenuGoods);


                    }
                }
            }
        }


        if (insert > 0) {
            return true;
        }
        return false;
    }

    //用户根据转态查询订单
    @Override
    public List<SelectUserOrderDTO> selectUserOrder(Long id, SelectUserOrderParam param, Integer type) {

        List<SelectUserOrderDTO> listDto = new ArrayList<>();//返回对象

        QueryWrapper<NewOrder> queryWrapper = new QueryWrapper<>();
        if (type == 0) {//用户
            queryWrapper.eq("user_id", id);
        }
        if (type == 1) {//商家
            queryWrapper.eq("shop_id", id);
        }


        Integer orderStatus = param.getOrderStatus();//订单状态（[1待上门、待配送、待发货]，2、完成）
        Integer payStatus = param.getPayStatus();//支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）
        Integer appointmentStatus = param.getAppointmentStatus();//预约状态（0预约中，1预约成功）






        //queryWrapper.eq("order_status",param.getOrderStatus());//订单状态（[1待上门、待配送、待发货]，2、完成）
        queryWrapper.eq("pay_status", param.getPayStatus());//支付状态（0未支付，1支付成功,2退款申请中，3退款成功，4拒绝退款）
        queryWrapper.eq("appointment_status", param.getAppointmentStatus());//预约状态（0预约中，1预约成功，2预约失败）
        List<NewOrder> newOrders = newOrderMapper.selectList(queryWrapper);//根据状态查询用户的所有订单






        for (NewOrder newOrder : newOrders) {
            SelectUserOrderDTO userOrderDTO = new SelectUserOrderDTO();//单个订单返回对象
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
                    //服务特点的集合
                    String serviceCharacteristicsIds = orderService.getServiceCharacteristicsIds();
                    List<ServiceCharacteristicsDto> list = characteristicsClient.getList(serviceCharacteristicsIds).getData();
                    serviceDto.setServiceCharacteristicsDtos(new ArrayList<>());
                    serviceDto.getServiceCharacteristicsDtos().addAll(list);
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
                    //服务特点的集合
                    String serviceCharacteristicsIds = orderGood.getServiceCharacteristicsIds();
                    List<ServiceCharacteristicsDto> list = characteristicsClient.getList(serviceCharacteristicsIds).getData();
                    orderGoodsDto.setServiceCharacteristicsDtos(new ArrayList<>());
                    orderGoodsDto.getServiceCharacteristicsDtos().addAll(list);

                    OrderGoodsDtos.add(orderGoodsDto);
                }


                QueryWrapper<OrderSetMenu> queryWrapper2 = new QueryWrapper<>();
                queryWrapper1.eq("order_id", newOrder.getId());
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
                            //服务特点的集合
                            String serviceCharacteristicsIds = orderSetMenuGood.getServiceCharacteristicsIds();
                            List<ServiceCharacteristicsDto> list = characteristicsClient.getList(serviceCharacteristicsIds).getData();
                            orderMenuGoodsDto.setServiceCharacteristicsDtos(new ArrayList<>());
                            orderMenuGoodsDto.getServiceCharacteristicsDtos().addAll(list);
                            orderMenuGoodsDtos.add(orderMenuGoodsDto);
                        }

                        SelectUserOrderMenuDto userOrderMenuDto = new SelectUserOrderMenuDto();
                        BeanUtils.copyProperties(orderSetMenu, userOrderMenuDto);
                        //服务特点的集合
                        String serviceCharacteristicsIds = orderSetMenu.getServiceCharacteristicsIds();
                        List<ServiceCharacteristicsDto> list = characteristicsClient.getList(serviceCharacteristicsIds).getData();
                        userOrderMenuDto.setServiceCharacteristicsDtos(new ArrayList<>());
                        userOrderMenuDto.getServiceCharacteristicsDtos().addAll(list);

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


    //用户删除订单
    @Override
    @Transactional
    public boolean deletedUserOrder(Long orderId) {
        //删除订单
        QueryWrapper<NewOrder> wrapper = new QueryWrapper<>();
        wrapper.eq("id", orderId);
        int delete = newOrderMapper.delete(wrapper);
        //删除订单服务
        QueryWrapper<OrderService> wrapper1 = new QueryWrapper<>();
        wrapper1.eq("order_id", orderId);
        int delete1 = orderServiceMapper.delete(wrapper1);


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

        return true;
    }

    //商家同意预约订单
    @Override
    public Boolean consentOrder(Long orderId) {
        NewOrder newOrder = new NewOrder();
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
        newOrder.setOrderStatus(1);//订单状态（[1待上门、待配送、待发货]，2、完成）
        newOrder.setId(param.getId());//订单id
        newOrder.setPayStatus(1);//支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）
        newOrder.setBillMonth(param.getBillMonth());//账单月份
        newOrder.setBillNum(param.getBillNum());//账单号
        newOrder.setBillRise(param.getBillRise());//账单抬头
        newOrder.setPayTime(param.getPayTime());//支付时间
        newOrder.setPayType(param.getPayType());//支付方式
        newOrder.setServeCode(OrderNoUtil.txOrder().toLowerCase());//生成验劵码转成大写
        int update = newOrderMapper.updateById(newOrder);
        if (update > 0) {
            return true;
        }
        return false;
    }

    //商家验卷接口
    public boolean acceptanceCheck(String shopId, String code) {
        QueryWrapper<NewOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("shop_id", shopId);
        queryWrapper.eq("order_status", 1);//未完成订单
        queryWrapper.eq("pay_status", 1);//确保已经支付
        queryWrapper.eq("appointment_status", 1);//确保预约成功
        queryWrapper.eq("serve_code", code);//验收码
        NewOrder newOrder = newOrderMapper.selectOne(queryWrapper);
        if (newOrder != null) {
            newOrder.setOrderStatus(2);//订单转态变为完成状态
            newOrder.setCommentStatus(0);//评论变为未评价状态
            int i = newOrderMapper.updateById(newOrder);
        }
        return true;

    }


}
