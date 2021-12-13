package com.jsy.client;

import cn.hutool.core.lang.Tuple;
import com.jsy.FeignConfiguration;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.SetMenuClientImpl;
import com.jsy.client.impl.ShoppingCartClientImpl;
import com.jsy.dto.QueryUserCartDto;
import com.jsy.dto.ShoppingCartDto;
import com.jsy.parameter.ShoppingCartParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(value = "shop-service-goods",fallback = ShoppingCartClientImpl.class,configuration = FeignConfiguration.class)
public interface ShoppingCartClient {
    /**
     * 添加商品/服务进入购物车
     * @param shoppingCartParam   userId、shopId、goodsId
     * @return
     */
    @PostMapping("/shoppingCart/addShoppingCart")
    CommonResult addShoppingCart(@RequestBody ShoppingCartParam shoppingCartParam);

    /**
     * 添加套餐进入购物车
     * @param shoppingCartParam userId、shopId、setMenuId
     * @return
     */
    @PostMapping("/shoppingCart/addSetMenu")
    CommonResult addSetMenu(@RequestBody ShoppingCartParam shoppingCartParam);

    /**
     * 清空购物车
     * @param shoppingCartParam  userId、shopId
     */
    @DeleteMapping("/shoppingCart/clearShoppingCart")
    CommonResult  clearCart (@RequestBody ShoppingCartParam shoppingCartParam);

    /**
     * 删除购物车里面单条商品
     */
    @DeleteMapping("/shoppingCart/delShoppingCart")
    CommonResult  delShoppingCart (@RequestParam("id") Long id);

    /**
     * 累减购物车
     * @param id
     */
    @DeleteMapping("/shoppingCart/reduceShoppingCart")
    CommonResult reduceShoppingCart(@RequestParam("id") Long id);

    /**
     * 查询购物车(店铺)  店铺id+token
     */
    @PostMapping("/shoppingCart/queryCart")
    CommonResult<ShoppingCartDto> queryCart(@RequestBody ShoppingCartParam shoppingCartParam);

    /**
     * 查询购物车(用户)  token
     */
    @PostMapping("/shoppingCart/queryCartAll")
    CommonResult<PageInfo<ShoppingCartDto>> queryCartAll(@RequestBody ShoppingCartParam shoppingCartParam);

    /**
     * 查询购物车用户的商品和店铺
     */
    @PostMapping("/shoppingCart/queryUserCart")
    CommonResult<QueryUserCartDto> queryUserCart(@RequestBody List<Long> shopIds);
}
