package com.jsy.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.domain.Order;
import com.jsy.domain.OrderDistribution;
import com.jsy.domain.User;
import com.jsy.domain.UserAddress;
import com.jsy.mapper.BaseSql;
import com.jsy.mapper.OrderDistributionMapper;
import com.jsy.service.IOrderDistributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2020-11-14
 */
@Service
public class OrderDistributionServiceImpl extends ServiceImpl<OrderDistributionMapper, OrderDistribution> implements IOrderDistributionService {

    @Autowired
    private IOrderDistributionService orderDistributionService;

    @Autowired
    private BaseSql baseSql;

    @Autowired
    private OrderDistributionMapper orderDistributionMapper;



    /**
     * 再订单地址表中存入订单的地址
     * @param userAddress
     * @param user
     * @param orderId
     */
    @Override
    public void insertByParams(UserAddress userAddress, User user, Long orderId) {
        //给一个OrderDistribution对象赋值
        OrderDistribution orderDistribution = replaysOrderDistribution(userAddress, user);
        orderDistribution.setOrderId(orderId);

        orderDistributionService.save(orderDistribution);
    }

    /**
     * 修改订单地址表
     * @param userAddress
     * @param user
     * @param orderId
     */
    @Override
    public void updateByParams(UserAddress userAddress, User user, Long orderId) {
        Long orderDistributionIdByOrderId = orderDistributionMapper.getByOId(orderId);
        //删除该订单地址
        orderDistributionService.removeById(orderDistributionIdByOrderId);

        //给一个OrderDistribution对象赋值
        OrderDistribution orderDistribution = replaysOrderDistribution(userAddress, user);
        orderDistribution.setOrderId(orderId);

        //新增新的地址
        orderDistributionService.save(orderDistribution);
    }

    @Override
    public int delByOrderId(Long id) {
        return orderDistributionMapper.delByOrderId(id);
    }
    /**
     * 将用户地址和用户信息给订单地址表
     * @param userAddress
     * @param user
     * @return
     */
    private static OrderDistribution replaysOrderDistribution(UserAddress userAddress, User user){
        OrderDistribution orderDistribution = new OrderDistribution();
        //orderDistribution.setOrderId();
        orderDistribution.setName(user.getName());
        orderDistribution.setPhone(user.getPhone());
        orderDistribution.setAddress(userAddress.getAddress());
        return orderDistribution;


    }
}
