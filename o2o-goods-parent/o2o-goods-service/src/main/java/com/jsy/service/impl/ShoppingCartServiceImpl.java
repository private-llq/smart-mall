package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.domain.Goods;
import com.jsy.domain.SetMenu;
import com.jsy.domain.ShoppingCart;
import com.jsy.dto.ShoppingCartDto;
import com.jsy.dto.ShoppingCartListDto;
import com.jsy.mapper.GoodsMapper;
import com.jsy.mapper.SetMenuMapper;
import com.jsy.mapper.ShoppingCartMapper;
import com.jsy.parameter.ShoppingCartParam;
import com.jsy.service.IShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lijin
 * @since 2021-11-11
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements IShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private GoodsMapper goodsMapper;


    @Autowired
    private SetMenuMapper setMenuMapper;

    /**
     * 添加商品进入购物车
     * @param shoppingCartParam
     * @return
     */
    @Override
    @Transactional
    public void addShoppingCart(ShoppingCartParam shoppingCartParam) {

        String userId = shoppingCartParam.getUserId();//用户id
        Long shopId = shoppingCartParam.getShopId();//商店id
        Long goodsId = shoppingCartParam.getGoodsId();//商品id
        //查询购物车
        ShoppingCart userCart = shoppingCartMapper.selectOne(new QueryWrapper<ShoppingCart>()
                .eq("user_id", userId)
                .eq("shop_id", shopId)
                .eq("goods_id", goodsId));
        if (Objects.isNull(userCart)){
            // 查询该商品或服务信息
            Goods goods = goodsMapper.selectOne(new QueryWrapper<Goods>().eq("id", goodsId).eq("is_putaway",1));
            if (Objects.isNull(goods)){
                throw new JSYException(-1,"该商品状态异常！");
            }

            ShoppingCart cartEntity = new ShoppingCart();
            cartEntity.setUserId(userId);
            cartEntity.setShopId(shopId);
            cartEntity.setGoodsId(goodsId);
            cartEntity.setNum(1);
            cartEntity.setTitle(goods.getTitle());
            cartEntity.setPrice(goods.getPrice());
            cartEntity.setDiscountPrice(goods.getDiscountPrice());
            cartEntity.setImages(goods.getImages());
            cartEntity.setDiscountState(goods.getDiscountState());
            shoppingCartMapper.insert(cartEntity);
        }else {
            Integer num = userCart.getNum();
            shoppingCartMapper.update(null,new UpdateWrapper<ShoppingCart>().eq("user_id",userId).eq("goods_id",goodsId).set("num",num+1));
        }


    }

    /**
     * 添加套餐进入购物车
     * @param shoppingCartParam userId、shopId、setMenuId
     * @return
     */
    @Override
    public void addSetMenu(ShoppingCartParam shoppingCartParam) {
        String userId = shoppingCartParam.getUserId();//用户id
        Long shopId = shoppingCartParam.getShopId();//商店id
        Long setMenuId = shoppingCartParam.getSetMenuId();//套餐id
        //查询购物车
        ShoppingCart userCart = shoppingCartMapper.selectOne(new QueryWrapper<ShoppingCart>()
                .eq("user_id", userId)
                .eq("shop_id", shopId)
                .eq("set_menu_id", setMenuId));

        if (Objects.isNull(userCart)){
            // 查询该套餐信息
            SetMenu setMenu = setMenuMapper.selectOne(new QueryWrapper<SetMenu>().eq("id", setMenuId).eq("state", 1));
            if (Objects.isNull(setMenu)){
                throw new JSYException(-1,"该套餐状态异常！");
            }

            ShoppingCart cartEntity = new ShoppingCart();
            cartEntity.setUserId(userId);
            cartEntity.setShopId(shopId);
            cartEntity.setSetMenuId(setMenuId);
            cartEntity.setNum(1);
            cartEntity.setTitle(setMenu.getName());
            cartEntity.setPrice(setMenu.getRealPrice());
            cartEntity.setDiscountPrice(setMenu.getSellingPrice());
            cartEntity.setImages(setMenu.getImages());
            cartEntity.setDiscountState(1);//套餐默认开启折扣
            shoppingCartMapper.insert(cartEntity);
        }else {
            Integer num = userCart.getNum();
            shoppingCartMapper.update(null,new UpdateWrapper<ShoppingCart>().eq("user_id",userId).eq("set_menu_id",setMenuId).set("num",num+1));
        }
    }


    /**
     * 清空购物车
     * @param shoppingCartParam
     */
    @Override
    @Transactional
    public void clearCart(ShoppingCartParam shoppingCartParam) {
        shoppingCartMapper.delete(new QueryWrapper<ShoppingCart>()
                .eq("user_id",shoppingCartParam.getUserId())
                .eq("shop_id",shoppingCartParam.getShopId())
        );
    }

    /**
     * 累减购物车
     * @param id
     */
    @Override
    public void reduceShoppingCart(Long id) {
        ShoppingCart cart = shoppingCartMapper.selectOne(new QueryWrapper<ShoppingCart>().eq("id", id));
        Integer num = cart.getNum();
        num--;
        shoppingCartMapper.update(null,new UpdateWrapper<ShoppingCart>().eq("id",id).set("num",num));
        if(num<=0){
            shoppingCartMapper.deleteById(id);
        }
    }


    /**
     * 查询购物车
     */
    @Override
    public ShoppingCartDto queryCart(ShoppingCartParam shoppingCartParam) {

        String userId = shoppingCartParam.getUserId();//用户id
        Long shopId = shoppingCartParam.getShopId();//商店id

        //商品总数量
        Integer sumGoods;

        //商品原价
        BigDecimal sumPrice = BigDecimal.ZERO;

        //商品支付价格
        BigDecimal payPrice=BigDecimal.ZERO;

        //购物车篮子
        List<ShoppingCartListDto> cartList;

        //返回对象
        ShoppingCartDto shoppingCartDto = new ShoppingCartDto();

        //查询某一个用户在某一个店 购物车列表
        List<ShoppingCart> carts = shoppingCartMapper.selectList(new QueryWrapper<ShoppingCart>().eq("user_id",userId).eq("shop_id", shopId));

        //对象转换
        cartList=BeansCopyUtils.listCopy(carts,ShoppingCartListDto.class);

        //商品总数
        sumGoods = carts.stream().mapToInt(ShoppingCart::getNum).sum();

        for (ShoppingCart cart : carts) {
            //按商品原价格算总价
            sumPrice= sumPrice.add(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNum())));

            //按实际支付价格算总价
            if (cart.getDiscountState()==0){//按原价算
                payPrice= payPrice.add(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNum())));
            }else {//按折扣价格算
                payPrice= payPrice.add(cart.getDiscountPrice().multiply(BigDecimal.valueOf(cart.getNum())));
            }
        }
        shoppingCartDto.setSumGoods(sumGoods);//商品总数
        shoppingCartDto.setPayPrice(payPrice);//商品支付价格
        shoppingCartDto.setCartList(cartList);//购物车列表
        shoppingCartDto.setSumPrice(sumPrice);//商品实际支付价格
        shoppingCartDto.setDiscountsPrice(sumPrice.subtract(payPrice));//优惠多少

        return shoppingCartDto;
    }



}
