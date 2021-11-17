package com.jsy.service.impl;

import com.jsy.domain.OrderGoods;
import com.jsy.mapper.OrderGoodsMapper;
import com.jsy.service.IOrderGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单商品表 服务实现类
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
@Service
public class OrderGoodsServiceImpl extends ServiceImpl<OrderGoodsMapper, OrderGoods> implements IOrderGoodsService {

}
