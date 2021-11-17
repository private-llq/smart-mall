package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.utils.OrderNoUtil;
import com.jsy.domain.*;
import com.jsy.dto.SelectUserOrderDTO;
import com.jsy.mapper.*;
import com.jsy.query.*;
import com.jsy.service.INewOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
        newOrder.setAppointmentStatus(0);//预约状态（0预约中，1预约成功）
        newOrder.setOrderStatus(1);//订单状态（[1待上门、待配送、待发货]，2、完成）
        newOrder.setPayStatus(0);//支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）
        newOrder.setCommentStatus(0);//是否评价0未评价，1评价（评价完成为订单完成）
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
    public List<SelectUserOrderDTO> selectUserOrder(Long id, SelectUserOrderParam param) {

        List<SelectUserOrderDTO> listDto =new ArrayList<>();
        QueryWrapper<NewOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",id);
        queryWrapper.eq("appointment_status",param.getAppointmentStatus());
        List<NewOrder> newOrders = newOrderMapper.selectList(queryWrapper);//查询用户的所有订单
        for (NewOrder newOrder : newOrders) {
            if (newOrder.getOrderType()==0) {//（0-服务类(只有服务)
                QueryWrapper<OrderService> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("order_id",newOrder.getId());
                List<OrderService> orderServices = orderServiceMapper.selectList(queryWrapper1);//查服务订单详情

            }
            if (newOrder.getOrderType()==1) {//1-普通类（套餐，单品集合）
                QueryWrapper<OrderGoods> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("order_id",newOrder.getId());
                List<OrderGoods> orderGoods = orderGoodsMapper.selectList(queryWrapper1);//商品的集合
                QueryWrapper<OrderSetMenu> queryWrapper2 = new QueryWrapper<>();
                queryWrapper1.eq("order_id",newOrder.getId());
                List<OrderSetMenu> orderSetMenus = orderSetMenuMapper.selectList(queryWrapper2);//套餐集合
                if (orderSetMenus.size()>0) {
                    for (OrderSetMenu orderSetMenu : orderSetMenus) {
                        QueryWrapper<OrderSetMenuGoods> queryWrapper3 = new QueryWrapper<>();
                        queryWrapper3.eq("order_menu_id",orderSetMenu.getId());
                        List<OrderSetMenuGoods> orderSetMenuGoods = orderSetMenuGoodsMapper.selectList(queryWrapper3);//套餐详情的集合

                    }
                }





            }
        }




        return null;
    }
}
