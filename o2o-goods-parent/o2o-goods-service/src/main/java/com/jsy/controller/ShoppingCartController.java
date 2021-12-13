package com.jsy.controller;
import cn.hutool.core.lang.Tuple;
import com.jsy.basic.util.PageInfo;
import com.jsy.dto.QueryUserCartDto;
import com.jsy.dto.ShoppingCartDto;
import com.jsy.parameter.ShoppingCartParam;
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
import java.util.Map;

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
     * 删除购物车里面单条商品
     */
    @DeleteMapping("delShoppingCart")
    public CommonResult  delShoppingCart (@RequestParam("id") Long id){
        shoppingCartService.removeById(id);
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
     * 累加购物车
     * @param id
     */
    @DeleteMapping("additionShoppingCart")
    public CommonResult additionShoppingCart(@RequestParam("id") Long id) {
        shoppingCartService.additionShoppingCart(id);
        return CommonResult.ok("success");
    }
    /**
     *  修改购物车数量
     * @param id
     */
    @GetMapping ("updateShoppingCart")
    public CommonResult updateShoppingCart(@RequestParam("id") Long id,@RequestParam("number") Integer number) {
        shoppingCartService.updateShoppingCart(id,number);
        return CommonResult.ok("success");
    }
    /**
     * 查询购物车(店铺)  店铺id+token
     */
    @PostMapping("queryCart")
    public CommonResult<ShoppingCartDto> queryCart(@RequestBody ShoppingCartParam shoppingCartParam){
        ShoppingCartDto shoppingCartDto= shoppingCartService.queryCart(shoppingCartParam);
        return CommonResult.ok(shoppingCartDto);
    }

    /**
     * 查询购物车(用户)  token
     */
    @PostMapping("queryCartAll")
    public CommonResult<PageInfo<ShoppingCartDto>> queryCartAll(@RequestBody ShoppingCartParam shoppingCartParam){
        PageInfo<ShoppingCartDto> pageInfo= shoppingCartService.queryCartAll(shoppingCartParam);
        return CommonResult.ok(pageInfo);
    }
    /**
     * 查询购物车用户的商品和店铺
     */
    @PostMapping("queryUserCart")
    public CommonResult<QueryUserCartDto> queryUserCart(@RequestBody List<Long> shopIds){
        QueryUserCartDto map= shoppingCartService.queryUserCart(shopIds);
        return CommonResult.ok(map);
    }
}
