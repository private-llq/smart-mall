package com.jsy.service.impl;

import com.jsy.domain.OrderService;
import com.jsy.mapper.OrderServiceMapper;
import com.jsy.service.IOrderServiceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单的服务表 服务实现类
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
@Service
public class OrderServiceServiceImpl extends ServiceImpl<OrderServiceMapper, OrderService> implements IOrderServiceService {

}
