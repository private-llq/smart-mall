package com.jsy.service.impl;
import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jsy.basic.util.MyPageUtils;
import com.jsy.basic.util.PageInfo;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.BeansCopyUtils;
import com.jsy.client.NewShopClient;
import com.jsy.domain.Goods;
import com.jsy.domain.NewShop;
import com.jsy.domain.SetMenu;
import com.jsy.domain.ShoppingCart;
import com.jsy.dto.NewShopDto;
import com.jsy.dto.ShoppingCartDto;
import com.jsy.dto.ShoppingCartListDto;
import com.jsy.mapper.GoodsMapper;
import com.jsy.mapper.SetMenuMapper;
import com.jsy.mapper.ShoppingCartMapper;
import com.jsy.parameter.ShoppingCartParam;
import com.jsy.service.IShoppingCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhsj.baseweb.support.ContextHolder;
import com.zhsj.baseweb.support.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
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


    @Autowired
    private NewShopClient shopClient;

    /**
     * 添加商品进入购物车
     * @param shoppingCartParam
     * @return
     */
    @Override
    @Transactional
    public void addShoppingCart(ShoppingCartParam shoppingCartParam) {

        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }

        Long userId = loginUser.getId();//用户id
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
            cartEntity.setIsSetMenu(0);//商品和服务
            //cartEntity.setIsVisitingService(goods.getIsVisitingService());//是否支持上门服务
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
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        Long userId = loginUser.getId();//用户id
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
            cartEntity.setIsSetMenu(1);//套餐
            //cartEntity.setIsVisitingService(setMenu.getIsVisitingService());//是否支持上门服务
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
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        Long userId = loginUser.getId();//用户id
        shoppingCartMapper.delete(new QueryWrapper<ShoppingCart>()
                .eq("user_id",userId)
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
     * 查询购物车（店铺）
     */
    @Override
    public ShoppingCartDto queryCart(ShoppingCartParam shoppingCartParam) {
        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }

        Long userId = loginUser.getId();//用户id
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
        NewShopDto newShop = shopClient.get(shopId).getData();//http://127.0.0.1:7006/newShop/get/1457644731467661314
        if (Objects.nonNull(newShop)){
            shoppingCartDto.setShopName(newShop.getShopName());//商店名称
        }
        shoppingCartDto.setSumGoods(sumGoods);//商品总数
        shoppingCartDto.setPayPrice(payPrice);//商品支付价格
        shoppingCartDto.setCartList(cartList);//购物车列表
        shoppingCartDto.setSumPrice(sumPrice);//商品实际支付价格
        shoppingCartDto.setDiscountsPrice(sumPrice.subtract(payPrice));//优惠多少

        return shoppingCartDto;
    }


    /**
     * 查询购物车(用户)
     * @param shoppingCartParam
     * @return
     */
    @Override
    public PageInfo<ShoppingCartDto> queryCartAll(ShoppingCartParam shoppingCartParam) {

        LoginUser loginUser = ContextHolder.getContext().getLoginUser();
        if (Objects.isNull(loginUser)){
            new JSYException(-1,"用户认证失败！");
        }
        //查询出用户在那些店铺有购物车
        Long userId = loginUser.getId();//用户id
        List<ShoppingCart> userCartList= shoppingCartMapper.selectList(new QueryWrapper<ShoppingCart>().eq("user_id", userId));
        HashSet<Long> shopIds = new HashSet<>();
        for (ShoppingCart cart : userCartList) {
            shopIds.add(cart.getShopId());
        }
        ArrayList<ShoppingCartDto> shoppingCartDtos = new ArrayList<>();
        ShoppingCartParam temp = new ShoppingCartParam();
        temp.setUserId(userId);
        for (Long shopId : shopIds) {
            temp.setShopId(shopId);
            shoppingCartDtos.add(this.queryCart(temp));
        }

        PageInfo<ShoppingCartDto> pageInfo = MyPageUtils.pageMap(shoppingCartParam.getPage(), shoppingCartParam.getSize(), shoppingCartDtos);
        return pageInfo;
    }
}
