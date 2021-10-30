package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.OrderDistribution;
import com.jsy.domain.User;
import com.jsy.domain.UserAddress;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2020-11-14
 */
public interface IOrderDistributionService extends IService<OrderDistribution> {


    void insertByParams(UserAddress userAddress, User user, Long orderId);

    void updateByParams(UserAddress userAddress, User user, Long orderId);

    int delByOrderId(Long id);



}
