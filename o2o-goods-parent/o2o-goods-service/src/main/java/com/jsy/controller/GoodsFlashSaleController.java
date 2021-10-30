package com.jsy.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.ShopAppSeckillClient;
import com.jsy.domain.Cart;
import com.jsy.domain.GoodsBasic;
import com.jsy.domain.ShopApplySeckill;
import com.jsy.mapper.CartMapper;
import com.jsy.service.impl.CartServiceImpl;
import com.jsy.service.impl.GoodsBasicServiceImpl;
import com.jsy.vo.RecordVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Objects;
@RestController
@RequestMapping("/goodsFlashSale")
@Api(tags = "商品抢购模块")
public class GoodsFlashSaleController {
    @Autowired
    private GoodsBasicServiceImpl GoodsBasicServiceImpl;

    @Autowired
    private CartServiceImpl CartServiceImpl;

    @Autowired
    private ShopAppSeckillClient ShopAppSeckillClient;

    @Autowired
    private CartMapper CartMapper;

    /**
     * 添加秒杀商品进入购物车
     * @param recordVo
     * @return
     */
    @Transactional
    @RequestMapping(value = "/AppRushPurchase", method = RequestMethod.POST)
    public CommonResult AppRushPurchase(@RequestBody RecordVo recordVo) {
        String userUuid = CurrentUserHolder.getUserEntity().getUid();//用户uuid
        String goodsUuid = recordVo.getGoodsUuid();//商品uuid
        String shopUuid = recordVo.getShopUuid();//商店uuid
        /**
         * 查询该商品是否存在，并且为上架状态
         */
        GoodsBasic goods = GoodsBasicServiceImpl.getOne(new QueryWrapper<GoodsBasic>().eq("uuid", goodsUuid).eq("shop_uuid", shopUuid).eq("is_marketable",1));
        if (Objects.isNull(goods)){
            return CommonResult.error(-1,"该商品不存在,可能已下架！");
        }
        BigDecimal price = goods.getPrice();//商品原价
        /**
         * 查询审核通过参加秒杀的商品信息
         */
        CommonResult<ShopApplySeckill> shopApplySeckillCommonResult = ShopAppSeckillClient.selectKillInfo(shopUuid, goodsUuid);
        ShopApplySeckill data = shopApplySeckillCommonResult.getData();
        if (data==null){
            throw new JSYException(-1,"没有查到该商品参加秒杀的信息!");
        }
        Integer num = data.getNum();//秒杀商品数量
        BigDecimal seckillPrice = data.getSeckillPrice();//秒杀价格
        Integer purchaseRestrictions = data.getPurchaseRestrictions();//限购数量
        Cart cart = new Cart();
        cart.setUserUuid(userUuid);
        cart.setShopUuid(shopUuid);
        cart.setGoodsUuid(goodsUuid);
        cart.setNum(1);
        cart.setKillGoods(1);//设为秒杀商品
        cart.setKillPrice(seckillPrice);
        cart.setPrice(price);
        cart.setKillStartTime(data.getStartTime());
        //通过抢购方式把商品添加进入购物车
        CartServiceImpl.addCart(cart);
        //查询购物车信息
        Cart userCart = CartMapper.selectOne(new QueryWrapper<Cart>().eq("user_uuid",userUuid).eq("goods_uuid",goodsUuid).eq("shop_uuid",shopUuid));
        Integer cartNum = userCart.getNum();//购物车已购数量
        //商品添加进购物车时不能大于商品剩余数量
        if (cartNum>num){
            throw  new JSYException(-1,"该商品剩余数量不足!");
        }
        //  每人至少限购一份
        if (userCart.getKillGoods()==1){
            //查询秒杀表的限购
            if (cartNum>purchaseRestrictions){//已购大于限购，抛异常
                CartMapper.update(cart,new UpdateWrapper<Cart>().eq("user_uuid",userUuid).eq("goods_uuid",goodsUuid).set("num",userCart.getNum()+num));
                throw new JSYException(-1,"每人限购" + data.getPurchaseRestrictions() + "份！");
            }
        }
        return CommonResult.ok("已抢购，进入购物车");
    }
}
