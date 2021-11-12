package com.jsy.service;

import com.jsy.domain.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.dto.ShoppingCartDto;
import com.jsy.parameter.ShoppingCartParam;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lijin
 * @since 2021-11-11
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
     * 查询购物车
     * @param shoppingCartParam
     * @return
     */
    ShoppingCartDto queryCart(ShoppingCartParam shoppingCartParam);

    /**
     * 添加套餐进入购物车
     * @param shoppingCartParam userId、shopId、setMenuId
     * @return
     */
    void addSetMenu(ShoppingCartParam shoppingCartParam);
}
