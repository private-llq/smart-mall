package com.jsy.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.Cart;
import com.jsy.domain.OrderCommodity;
import com.jsy.dto.OrderCommodityDto;
import com.jsy.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yu
 * @since 2020-11-12
 */
public interface IOrderCommodityService extends IService<OrderCommodity>, BaseMapper<OrderCommodityDto, OrderCommodity> {

    void setCommoditysByOid(String shopGoodsIds, String orderUuid);

    void updateCommodityBySids(String shopGoodsIds, String orderUuid);

    List<OrderCommodityDto> getCommoditysByOid(Long orderId);

    void delCommodityByOid(Long orderId);

    void saveByCart(List<Cart> cartList, String orderUuid);

    List<OrderCommodity> getByOid(String orderUuid);

    int delete(String orderUuid);

    int monthSales(QueryWrapper queryWrapper);
}
