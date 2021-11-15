package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.domain.Cart;
import com.jsy.dto.CartDTO;
import com.jsy.dto.ShoppingCartDto;
import com.jsy.mapper.CartMapper;
import com.jsy.parameter.ShoppingCartParam;
import com.jsy.query.CartQuery;
import com.jsy.service.IShoppingCartService;
import com.jsy.domain.ShoppingCart;
import com.jsy.query.ShoppingCartQuery;
import com.jsy.basic.util.PageList;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.jsy.basic.util.vo.CommonResult;

import java.util.ArrayList;
import java.util.List;

import static com.jsy.basic.util.utils.CurrentUserHolder.getUserEntity;

@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    public IShoppingCartService shoppingCartService;

    /**
     * 添加商品/服务进入购物车
     * @param shoppingCartParam   userId、shopId、goodsId
     * @return
     */
    @PostMapping("addShoppingCart")
    public CommonResult addShoppingCart(@RequestBody ShoppingCartParam shoppingCartParam){

        shoppingCartService.addShoppingCart(shoppingCartParam);
        return CommonResult.ok("success");
    }

    /**
     * 添加套餐进入购物车
     * @param shoppingCartParam userId、shopId、setMenuId
     * @return
     */
    @PostMapping("addSetMenu")
    public CommonResult addSetMenu(@RequestBody ShoppingCartParam shoppingCartParam){
        shoppingCartService.addSetMenu(shoppingCartParam);
        return CommonResult.ok("success");
    }

    /**
     * 清空购物车
     * @param shoppingCartParam  userId、shopId
     */
    @DeleteMapping("clearShoppingCart")
    public CommonResult  clearCart (@RequestBody ShoppingCartParam shoppingCartParam){
        shoppingCartService.clearCart(shoppingCartParam);
        return CommonResult.ok("success");
    }

    /**
     * 累减购物车
     * @param id
     */
    @DeleteMapping("reduceShoppingCart")
    public CommonResult reduceShoppingCart(@RequestParam("id") Long id) {
        shoppingCartService.reduceShoppingCart(id);
        return CommonResult.ok("success");
    }


    /**
     * 查询购物车
     */
    @PostMapping("queryCart")
    public CommonResult<ShoppingCartDto> queryCart(@RequestBody ShoppingCartParam shoppingCartParam){
        ShoppingCartDto shoppingCartDto= shoppingCartService.queryCart(shoppingCartParam);
        return CommonResult.ok(shoppingCartDto);
    }
}