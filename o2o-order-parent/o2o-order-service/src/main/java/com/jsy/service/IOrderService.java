package com.jsy.service;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.annotation.ServiceAnnotation;
import com.jsy.domain.Order;
import com.jsy.domain.UserAddress;
import com.jsy.dto.*;
import com.jsy.mapper.BaseMapper;
import com.jsy.vo.OrderVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2020-11-12
 */
public interface IOrderService extends IService<Order> , BaseMapper<OrderDto, Order> {


    /*
     * @return int
     * @Author 国民探花
     * @Description
     * @Date 2021-05-14 13:57
     * @Param [uuid]
     **/
    int deleteById(String uuid);

    /**
     * 根据订单id找到订单商品
     * @param uuid
     * @return
     */
    List<OrderCommodityDto> findGoods(String uuid);

    /**
     * 根据用户id找到未完成订单
     * @param uuid
     * @return
     */
    List<OrderDto> selectWait(String uuid);

    /**
     * 根据用户id找到退款的订单
     * @param uuid
     * @return
     */
    List<OrderDto> selectBack(String uuid);

    /**
     * 根据用户id找到历史订单
     * @param uuid
     * @return
     */
    List<OrderDto> getHistory(String uuid);

    /**
     * 根据用户id和店家id找到未支付订单
     * @param userUuid
     * @param uuid
     * @return
     */
    List<OrderDto> getByUid(String userUuid, String uuid);

    /**
     * 根据店家uuid查找Order信息
     * @param uuid
     * @return
     */
    List<OrderDto> getBillBySid(String uuid);

    /**
     * 根据id将订单改为待删除状态
     * @param uuid
     */
    int changeStatus(String uuid);

    /**
     * 删除所有数据库待删除订单,并删除该订单商品
     */
    int deleteByStatus();

    /**
     * 根据用户uuid找到Order
     * @param userUuid
     * @return
     */
    List<OrderInfoDto> getByUserUuid(String userUuid);

    /**
     * 根据uuid完成订单--修改订单状态
     * @param uuid
     */
    int finishOrderByUuid(String uuid);

    /**
     * 将订单改为待删除订单
     * @param uuid
     */
    int deleteOrderByUuid(String uuid);

    /**
     * 根据用户uuid找到退款中订单
     * @param uuid
     * @return
     */
    List<OrderDto> backOrderByUuid(String uuid);

    /**
     * 根据订单uuid找到订单
     * @param uuid
     * @return
     */
    OrderDto getByUuid(String uuid);

    BigDecimal getMoney(QueryWrapper<Order> queryWrapper);

    List<JSONObject> getRedpacket(QueryWrapper<Order> queryWrapper);

    int updateEid(String orderUuid);

    @ServiceAnnotation(description = "save")
    Order save(OrderVo ordervo, UserAddress userAddress, CartDTO cartDTO, String uuid);

    List<OrderInfoDto> getRunOrder(String uuid);

    int setRunOrder(String uuid);

    int cancelOrder(String uuid);

    int cancelOrderList(List<String> str);

    List<OrderDto> backOrderBySUuid(String uuid);

    int finishCancelOrder(String uuid);



    int payedOrder(String uuid);

    /**
     * 根据statuId 和 订单uuid改变订单状态
     * 订单状态 0:未支付,1:已支付,2:正在配送,3:已完成,4:退款中,5:已退款,6:待删除,7:作废
     * @param stateId
     * @param uuid
     * @return
     */
    int changeStatusByUid(Integer stateId,String uuid);

    List<Order> getByStateId(String state);

    OrderDto selectByOrderNum(String orderNum);

    NewUserDto statisticsNewUser(String shopUuid);

    ActivityDTO statisticalActivities(String shopUuid);

    int generateCode(OrderDto orderDto);

    int changeUsed(String serviceCode,String uuid);

    int noCancelOrder(String uuid);

    List<OrderDto> waitPayed(String uid);

    List<OrderDto> waitUsed(String uid);

    List<OrderDto> ownOrder(String uid);

    Map<String,String> getTurnover(QueryWrapper<Order> queryWrapper);

    int selectBackCount(String uid);

    int acceptOrder(String uuid, String uid);

    IPage<OrderInfoDto> pageUserState(Page page, QueryWrapper<Order> queryWrapper);

    Order saveOrder(OrderVo orderVo);

    List<Order> billDetails();

}
