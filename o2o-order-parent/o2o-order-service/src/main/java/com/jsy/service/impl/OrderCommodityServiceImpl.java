package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.client.GoodsBasicFeign;
import com.jsy.domain.Cart;
import com.jsy.domain.GoodsBasic;
import com.jsy.domain.OrderCommodity;
import com.jsy.dto.OrderCommodityDto;
import com.jsy.mapper.OrderCommodityMapper;
import com.jsy.service.IOrderCommodityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yu
 * @since 2020-11-12
 */
@Service
public class OrderCommodityServiceImpl extends ServiceImpl<OrderCommodityMapper, OrderCommodity> implements IOrderCommodityService {

    @Autowired
    private GoodsBasicFeign goodsBasicFeign;

    @Autowired
    private IOrderCommodityService orderCommodityService;

    @Autowired
    private OrderCommodityMapper orderCommodityMapper;

    /**
     * 在订单商品表中存入订单对应的多条订单商品
     * @param shopGoodsIds
     * @param orderUuid
     */
    @Override
    public void setCommoditysByOid(String shopGoodsIds,String orderUuid) {

        //获取[订单商品id，订单商品数量]数组
        String[] arrayGoodsId = shopGoodsIds.split(",");

        List<OrderCommodity> list = new ArrayList<>();
        for (String arrayId : arrayGoodsId) {
            System.out.println("获取[订单商品id，订单商品数量]数组" + Arrays.toString(arrayGoodsId));
            /*获取[订单A商品id:订单A商品数量]*/
            String[] arrayNum = arrayId.split(":");
            System.out.println("获取[订单A商品id，订单A商品数量]" + Arrays.toString(arrayNum));
            /*获取商品信息    arrayNum[0]为商品id，arrayNum[1]为商品数量*/
            //GoodsBasic goods = goodBasicService.getById(Long.valueOf(arrayNum[0]));
            GoodsBasic goods = goodsBasicFeign.getGoods(arrayNum[0]);
            /*给orderCommodity赋值*/
            OrderCommodity orderCommodity = replaysOrderCommodity(goods);
            orderCommodity.setNum(Integer.valueOf(arrayNum[1]));
            orderCommodity.setOrderUuid(orderUuid);
            list.add(orderCommodity);
        }
            orderCommodityService.saveBatch(list);
    }
    /**
     * 在订单商品表中修改订单对应的多条订单商品
     * @param shopGoodsIds
     * @param orderUuid
     */
    @Override
    public void updateCommodityBySids(String shopGoodsIds,String orderUuid) {
        /*获取[订单商品id，订单商品数量]数组*/
        String[] arrayGoodsId = shopGoodsIds.split(",");
        /*先删除该订单的订单商品，再新增*/
        orderCommodityMapper.delCommodityByUid(orderUuid);

        List<OrderCommodity> list = new ArrayList<>();
        for (String arrayId : arrayGoodsId) {
            /*获取[订单A商品id，订单A商品数量]*/
            String[] arrayNum = arrayId.split(":");
            System.out.println("获取[订单A商品id，订单A商品数量]" + Arrays.toString(arrayNum));
            /*获取商品信息    arrayNum[0]为商品id，arrayNum[1]为商品数量*/
            GoodsBasic goods = goodsBasicFeign.getGoods(arrayNum[0]);
            /*给orderCommodity赋值*/
            OrderCommodity orderCommodity = replaysOrderCommodity(goods);
            orderCommodity.setNum(Integer.valueOf(arrayNum[1]));
            orderCommodity.setOrderUuid(orderUuid);
            list.add(orderCommodity);
        }
        orderCommodityService.saveBatch(list);
    }

    /**
     * 根据订单id查询订单商品
     * @param orderId
     * @return
     */
    @Override
    public List<OrderCommodityDto> getCommoditysByOid(Long orderId) {

        List<OrderCommodity> list = orderCommodityMapper.getCommoditysByOid(orderId);
        List<OrderCommodityDto> orderCommodityDtos = toListDto(list);
        return orderCommodityDtos;
    }

    /**
     * 根据订单id删除订单商品表的商品
     * @param orderId
     */
    @Override
    public void delCommodityByOid(Long orderId) {
        orderCommodityMapper.delCommodityByOid(orderId);
    }

    @Override
    public List<OrderCommodity> getByOid(String orderUuid) {
        return this.list(new QueryWrapper<OrderCommodity>().eq("order_uuid",orderUuid));
    }

    @Override
    public int delete(String orderUuid) {
        return orderCommodityMapper.delete(new QueryWrapper<OrderCommodity>().eq("order_uuid",orderUuid));
    }

    @Override
    public int monthSales(QueryWrapper queryWrapper) {
        return orderCommodityMapper.monthSales(queryWrapper);
    }

    @Override
    public void saveByCart(List<Cart> cartList, String orderUuid) {
        List<OrderCommodity> list = new ArrayList<>();

        for (Cart cart : cartList){
            OrderCommodity orderCommodity = new OrderCommodity();
            orderCommodity.setPrice(cart.getPrice());
            orderCommodity.setGoodsUuid(cart.getGoodsUuid());
            orderCommodity.setNum(cart.getNum());
            orderCommodity.setName(cart.getTitle());
            orderCommodity.setImage(cart.getImage());
            orderCommodity.setOrderUuid(orderUuid);
            list.add(orderCommodity);
        }
        this.saveBatch(list);
    }

    /**
     * 将goods商品部分信息传递给orderCommodity
     * @param goods
     * @return
     */
    private static OrderCommodity replaysOrderCommodity(GoodsBasic goods){
        OrderCommodity orderCommodity = new OrderCommodity();

        orderCommodity.setName(goods.getTitle());
        //orderCommodity.setNum();
        //orderCommodity.setOrderId();
        orderCommodity.setPrice(goods.getPrice());
        orderCommodity.setImage(goods.getImagesUrl());
        return orderCommodity;

    }
    @Override
    public OrderCommodityDto toDto(OrderCommodity entity) {
        if (Objects.isNull(entity)){
            return null;
        }
        OrderCommodityDto orderCommodityDto = new OrderCommodityDto();
        orderCommodityDto.setId(entity.getId());
        orderCommodityDto.setName(entity.getName());
        orderCommodityDto.setNum(entity.getNum());
        orderCommodityDto.setOrderUuid(entity.getOrderUuid());
        orderCommodityDto.setPrice(entity.getPrice());
        orderCommodityDto.setImage(entity.getImage());
        return orderCommodityDto;
    }
    @Override
    public OrderCommodity toEntity(OrderCommodityDto dto) {
        if (Objects.isNull(dto)){
            return null;
        }
        OrderCommodity orderCommodity = new OrderCommodity();
        orderCommodity.setId(dto.getId());
        orderCommodity.setName(dto.getName());
        orderCommodity.setNum(dto.getNum());
        orderCommodity.setOrderUuid(dto.getOrderUuid());
        orderCommodity.setPrice(dto.getPrice());
        orderCommodity.setImage(dto.getImage());
        return orderCommodity;
    }
    @Override
    public List<OrderCommodity> toListEntity(List<OrderCommodityDto> list) {
        if (list.isEmpty()){
            return null;
        }
        List<OrderCommodity> orderCommodities = new ArrayList<OrderCommodity>(list.size());
        for (OrderCommodityDto orderCommodityDto : list){
            OrderCommodity orderCommodity = toEntity(orderCommodityDto);
            orderCommodities.add(orderCommodity);
        }
        return orderCommodities;
    }

    @Override
    public List<OrderCommodityDto> toListDto(List<OrderCommodity> list) {
        if (list.isEmpty()){
            return null;
        }
        List<OrderCommodityDto> orderCommodityDtos = new ArrayList<OrderCommodityDto>(list.size());
        for (OrderCommodity orderCommodity : list){
            OrderCommodityDto orderCommodityDto = toDto(orderCommodity);
            orderCommodityDtos.add(orderCommodityDto);
        }
        return orderCommodityDtos;
    }


}
