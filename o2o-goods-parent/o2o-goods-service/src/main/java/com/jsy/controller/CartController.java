package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.client.ShopClient;
import com.jsy.domain.Cart;
import com.jsy.domain.ShopInfo;
import com.jsy.dto.CartDTO;
import com.jsy.query.CartQuery;
import com.jsy.service.ICartService;
import com.jsy.vo.CartVo;
import com.zhsj.basecommon.vo.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import netscape.javascript.JSException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
@Api(tags = "购物车")
public class CartController {
    @Autowired
    private  ICartService ICartService;


    /**
     * 添加购物车
     * @return
     */
    @ApiOperation(value = "添加商品进入购物车",httpMethod ="POST",response=CommonResult.class,notes="1.0")
    @ApiParam(required = true, name ="Cart",value = "传入购物车对象")
    @PostMapping("/addCart")
    public CommonResult addCart(@ApiParam("商店uuid,商品uuid,商品数量")@RequestBody Cart cart){
        this.ICartService.addCart(cart);
        return CommonResult.ok("success");
    }
    /**
     * 查询购物车
     * @return
     */
    @ApiOperation("查询购物车商品")
    @PostMapping("/queryCart")
    public CommonResult queryCart(@ApiParam("商店uuid,用户token")@RequestBody CartQuery CartQuery){
        CartDTO cartDTO = ICartService.queryCart(CartQuery);
        return CommonResult.ok(cartDTO);
    }
    /**
     * 查询购物车 2.0
     * 结合满减活动、红包、折扣商品、秒杀商品、新客（首单立减）
     * 进行新的结算方案
     */
    @ApiOperation("查询购物车商品")
    @PostMapping("/queryCart2")
    public CommonResult queryCart2(@ApiParam("商店uuid,用户token")@RequestBody CartQuery CartQuery){
        CartDTO cartDTO = ICartService.queryCart2(CartQuery);
        return CommonResult.ok(cartDTO);
    }


    /**
     * 展示该用户所有店铺购物车
     * 店铺信息->店铺购物车信息
     */
    @ApiOperation("展示所有店铺购物车")
    @PostMapping("/shopListCart")
    public CommonResult shopListCart(){
        UserEntity userEntity = CurrentUserHolder.getUserEntity();
        String userUuid = userEntity.getUid();
        List<Cart> list = ICartService.list(new QueryWrapper<Cart>().eq("user_uuid", userUuid));

        if (list.size()==0){
            return CommonResult.ok(list);
        }

        ArrayList<String> objects = new ArrayList<>();
        for (Cart cart : list) {
            objects.add(cart.getShopUuid());
        }
        List<String> shopIds = objects.stream().distinct().collect(Collectors.toList());//所有店铺ids

        List<ShopInfo> shopList= ICartService.getShopList(shopIds);

        for (ShopInfo shopInfo : shopList) {
            CartQuery cartQuery = new CartQuery();
            cartQuery.setShopUuid(shopInfo.getUuid());
            CartDTO cartDTO = ICartService.queryCart2(cartQuery);
            shopInfo.setCartDTO(cartDTO);
        }

        return CommonResult.ok(shopList);
    }

    /**
     * @param cart
     * @return
     */
    @ApiOperation("修改购物车里一个商品的数量")
    @PutMapping("/updateNum")
    public CommonResult  updateNum(@ApiParam("商店ID,商品ID,商品数量")@RequestBody Cart cart){
        this.ICartService.updateCartNum(cart);
        return CommonResult.ok("success");
    }

    /**
     * 累减购物车商品数量
     * @param
     * @return
     */
    @ApiOperation("累减购物车里一个商品的数量")
    @PostMapping("/subtractNum/{cartUuid}")
    public CommonResult subtractNum(@ApiParam("购物车id")@PathVariable("cartUuid") String cartUuid){
        this.ICartService.subtractNum(cartUuid);
        return CommonResult.ok("success");
    }
    /**
     * 删除购物车商品
     * @param cartUuid
     * @return
     */
    @ApiOperation("删除购物车里面的一个商品")
    @DeleteMapping("{shopUuid}/{goodsUuid}")
    public CommonResult deleteCart(@RequestParam ("cartUuid") String cartUuid) {
        this.ICartService.deleteCart(cartUuid);
        return CommonResult.ok("success");
    }

    /**
     * 清空购物车
     * @param shop_uuid
     */
    @ApiOperation("清空当前用户的购物车商品")
    @DeleteMapping("{shopUuid}")
    public CommonResult  clearCart(@ApiParam("商店ID")@PathVariable("shopUuid")String shop_uuid){
        this.ICartService.clearCart(shop_uuid);
        return CommonResult.ok("success");
    }

    /**
     * 批量删除购物车
     * @param cartIds 以,隔开
     * @return
     */
    @ApiOperation("批量删除购物车商品")
    @DeleteMapping("/BatchDeleteCart")
    public CommonResult  BatchDeleteCart(@RequestParam("cartIds") String cartIds){
       this.ICartService.BatchDeleteCart(cartIds);
       return CommonResult.ok("success");
    }
}