package com.jsy.service;

import cn.hutool.core.lang.Tuple;
import com.jsy.basic.util.PageInfo;
import com.jsy.domain.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.ShoppingCartDto;
import com.jsy.parameter.ShoppingCartParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2021-11-16
 */
public interface IShoppingCartService extends IService<ShoppingCart> {

    /**
     * 添加商品进入购物车
     * @param shoppingCartParam
     * @return
     */
    void addShoppingCart(ShoppingCartParam shoppingCartParam);

    /**
     * 清空购物车
     * @param shoppingCartParam
     */
    void clearCart(ShoppingCartParam shoppingCartParam);

    /**
     * 累减购物车
     * @param id
     */
    void reduceShoppingCart(Long id);


    /**
     * 添加套餐进入购物车
     * @param shoppingCartParam userId、shopId、setMenuId
     * @return
     */
    void addSetMenu(ShoppingCartParam shoppingCartParam);


    /**
     * 查询购物车(店铺)
     * @param shoppingCartParam
     * @return
     */
    ShoppingCartDto queryCart(ShoppingCartParam shoppingCartParam);


    /**
     * 查询购物车(用户)
     * @param shoppingCartParam
     * @return
     */
    PageInfo<ShoppingCartDto> queryCartAll(ShoppingCartParam shoppingCartParam);
     //累减
    void additionShoppingCart(Long id);
   // 修改购物车数量
    void updateShoppingCart(Long id, Integer number);

    /**
     * 查询购物车用户的商品和店铺
     */
    Tuple queryUserCart(List<Long> shopIds);
}
