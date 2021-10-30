package com.jsy.client;

import com.jsy.FeignConfiguration;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.impl.CartClientImpl;
import com.jsy.domain.Cart;
import com.jsy.dto.CartDTO;
import com.jsy.query.CartQuery;
import com.jsy.vo.CartVo;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "SERVICE-GOODS",fallback = CartClientImpl.class,configuration = FeignConfiguration.class)
public interface CartClient {
    
    /**
     */
    @PostMapping("/cart/addCart")
    CommonResult addCart(@ApiParam("商店uuid,商品uuid,商品数量") @RequestBody Cart cart);
    /**
     * 查询购物车
     * @return
     */
    @PostMapping(value = "/cart/queryCart")
    CommonResult queryCart(@ApiParam("商店uuid，商品uuid") @RequestBody CartQuery CartQuery);


    @ApiOperation("查询购物车商品")
    @PostMapping("/cart/queryCart2")
    public CommonResult queryCart2(@ApiParam("商店uuid,用户token")@RequestBody CartQuery CartQuery);
    /**
     * 修改购物车商品数量
     * @param cart
     * @return
     */
    @PutMapping("/cart/updateNum")
     CommonResult<CartDTO> updateNum(@ApiParam("商店ID,商品ID,商品数量") @RequestBody Cart cart);
    /**
     * 删除购物车商品
     * @param goodsUuid
     * @return
     */
    @DeleteMapping("/cart/{shopUuid}/{goodsUuid}")
     CommonResult deleteCart(@ApiParam("商店ID") @PathVariable("shopUuid") String shopUuid,
                             @ApiParam("商品ID") @PathVariable("goodsUuid") String goodsUuid);
    /**
     * 清空购物车
     * @param shop_uuid
     */
    @DeleteMapping("/cart/{shopUuid}")
     CommonResult  clearCart(@ApiParam("商店ID") @PathVariable("shopUuid") String shop_uuid);
    /**
     * 批量删除购物车
     * @param cartVo
     * @return
     */
    @DeleteMapping("/cart/BatchDeleteCart")
     CommonResult  BatchDeleteCart(@ApiParam("商品ID字符串和商店ID") @RequestBody CartVo cartVo);

}
