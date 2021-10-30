package com.jsy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jsy.domain.Cart;
import com.jsy.domain.ShopInfo;
import com.jsy.dto.CartDTO;
import com.jsy.query.CartQuery;
import com.jsy.vo.CartVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 * @author lijin
 * @since 2020-11-23
 */
public interface ICartService extends IService<Cart> {

     void addCart(Cart cart);

     CartDTO queryCart(CartQuery CartQuery);

     void updateCartNum(Cart cart);

     void deleteCart(String cartUuid);

     void  clearCart(String shopUuid);

     void  BatchDeleteCart(String cartIds);

     void subtractNum(String cartUuid);

    CartDTO queryCart2(CartQuery cartQuery);

    List<ShopInfo> getShopList(List<String> collect1);
}
