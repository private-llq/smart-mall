package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.client.*;
import com.jsy.controller.GoodsBasicController;
import com.jsy.controller.GoodsOtherCostController;
import com.jsy.domain.*;
import com.jsy.dto.CartDTO;
import com.jsy.dto.SelectGoodsOtherCostByGoodsUuidDto;
import com.jsy.mapper.CartMapper;
import com.jsy.parameter.SelectGoodsOtherCostByGoodsUuidParam;
import com.jsy.query.CartQuery;
import com.jsy.service.ICartService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.jsy.basic.util.utils.CurrentUserHolder.getUserEntity;
/**
 * @author lijin
 * @since 2020-11-23
 */
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart> implements ICartService {
    @Autowired
    private  CartMapper CartMapper;
    @Autowired
    private GoodsBasicController goodsBasicController;
    @Autowired
    private ActivityClient activityClient;
    @Autowired
    private UserRedpackClient UserRedpackClient;
    @Autowired
    private FileClient fileClient;
    @Autowired
    private TimeTableClient timeTableClient;
    @Autowired
    private ShopClient shopClient;
    @Autowired
    private GoodsOtherCostController goodsOtherCostController;
    @Resource
    private ShopInfoClient ShopInfoClient;



    /**
     * 添加商品进入购物车
     * @param cart
     */
    @Transactional
    public void addCart(Cart cart){
        UserEntity userEntity = getUserEntity();
        String userUuid = userEntity.getUid();
        String goodsUuid = cart.getGoodsUuid();
        String shopUuid = cart.getShopUuid();
        Cart userCart = CartMapper.selectOne(new QueryWrapper<Cart>().eq("user_uuid",userUuid).eq("goods_uuid",goodsUuid).eq("shop_uuid",shopUuid));
        Integer num = cart.getNum();
        GoodsBasic goods = goodsBasicController.getGoods(goodsUuid);

        if (Objects.isNull(goods)){
            throw  new JSYException(-1,"该商品不存在");
        }
        if (cart.getKillGoods()==1&&goods.getNum()<num) {
            throw  new JSYException(-1,"购买数量不足");
        }
        if (userCart!=null && goodsUuid.equals(userCart.getGoodsUuid())){
            //修改时商品总价
            BigDecimal sumPrice = goods.getPrice().multiply(BigDecimal.valueOf(userCart.getNum() + num));
            CartMapper.update(cart,new UpdateWrapper<Cart>().eq("user_uuid",userUuid).eq("goods_uuid",goodsUuid).set("num",userCart.getNum()+num));
        }else{
            cart.setUserUuid(userUuid);
            cart.setNum(num);
            cart.setTitle(goods.getTitle());
            cart.setOwnSpec(goods.getOwnSpec());
            cart.setPrice(goods.getPrice());
            cart.setDiscountPrice(goods.getDiscountPrice());
            cart.setGoodsUuid(goodsUuid);
            String pic = Arrays.asList(goods.getImagesUrl().split(";")).get(0);
            cart.setImage(StringUtils.isBlank(goods.getImagesUrl()) ? "" : pic);
            cart.setUuid(UUIDUtils.getUUID());
            cart.setKillGoods(0);//普通商品
            /********************折扣商品******************************/
            cart.setDiscount(goods.getDiscount());//商品的折扣(0.10-0.99)
            cart.setDiscountEndTime(goods.getDiscountEndTime());//折扣结束时间
            cart.setDiscountPrice(goods.getDiscountPrice());//折扣价格
            cart.setDiscountStartTime(goods.getDiscountStartTime());//折扣开始时间
            cart.setDiscountStatus(goods.getDiscountStatus());//1按照折扣0按照折扣价格
            cart.setActivityStatus(goods.getActivityStatus());//折扣活动状态(1启用0未启动)
            CartMapper.insert(cart);
        }
    }
    /**
     * 查询购物车
     * @param CartQuery
     * @return
     */
    @Transactional
    public CartDTO queryCart(CartQuery CartQuery) {//CartQuery没作分页
        UserEntity userEntity = getUserEntity();
        String userUuid = userEntity.getUid();
        String shopUuid = CartQuery.getShopUuid();
        //查询某一个用户在某一个店 购物车列表
        List<Cart> carts = CartMapper.selectList(new QueryWrapper<Cart>().eq("user_uuid",userUuid).eq("shop_uuid", shopUuid));
        //商品总价
        BigDecimal sumPrice = new BigDecimal(0);
        //商品总数量
        Integer sumGoods=0;
        //返回对象
        CartDTO cartDTO = new CartDTO();
        //满减活动flag
        Boolean flag =true;
        //购物车商品图片List
        ArrayList<String> imges = new ArrayList<>();

        //查询时间表
        CommonResult<Timetable> timetableCommonResult = timeTableClient.selectTimeLong();
        LocalDateTime endTime = timetableCommonResult.getData().getEndTimeLong();//活动结束时间

        //循环一个用户在某一家店的购物车
        for (Cart cart : carts) {
            imges.add(cart.getImage());
            if (cart.getKillGoods()==1){//如果是秒杀商品
                sumPrice=sumPrice.add(cart.getKillPrice().multiply(BigDecimal.valueOf(cart.getNum())));//秒杀单价*数量
                cartDTO.setKillGoods(1);
                if (endTime.isBefore(LocalDateTime.now())){//活动结束时间没有过
                    cart.setKillState(1);//当前时间已过活动结束时间 该秒杀商品失效
                }else {
                    cart.setKillState(0);//未过时有效   ps://前端根据cart状态0,1进行判断
                }
            }else if (cart.getDiscountPrice()==null || cart.getDiscountPrice()==new BigDecimal(0)){//0和null都为商品没有折扣（ps:如果想免费直接原价设为0）
                sumPrice=sumPrice.add(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNum())));//原价单价*数量
            }else if (new BigDecimal(0).compareTo(cart.getDiscountPrice()) < 0){//商品有折扣,并且折扣价格大于0
                sumPrice=sumPrice.add(cart.getDiscountPrice().multiply(BigDecimal.valueOf(cart.getNum())));//折扣单价*数量
                flag =false; //不参加活动标志
            }
            else {
                //cartDTO.setKillGoods(0);
                sumPrice=sumPrice.add(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNum())));//原价单价*数量
            }
            sumGoods=sumGoods+cart.getNum();//商品总数量
            if (cart.getKillGoods()==1){//如果为秒杀商品
                cart.setSumCartPriceShow(cart.getKillPrice().multiply(BigDecimal.valueOf(cart.getNum()))); //(购物车里面每行商品的秒杀单价*数量)
            }else {
                cart.setSumCartPriceShow(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNum()))); //(购物车里面每行商品的单价*数量)
            }
        }

        //接受前端传过来的红包uuid
        String redPackageUuid = CartQuery.getRedPackageUuid();
        //红包金额
        Integer redpackageMoney =0;
        if (Objects.nonNull(redPackageUuid)){
            //查询红包的价格
            CommonResult commonResult = UserRedpackClient.get(redPackageUuid);
            if (Objects.isNull(commonResult.getData())){
                throw new JSYException(-1,"红包参数异常！");
            }
            ObjectMapper mapper=new ObjectMapper();
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.registerModule(new JavaTimeModule());
            UserRedpacket userRedpacket =mapper.convertValue(commonResult.getData(),UserRedpacket.class);
            redpackageMoney = userRedpacket.getMoney();
        }
        //查询是否有满足价格的活动
        CommonResult<Activity> result = activityClient.findOne(shopUuid , sumPrice);
        Activity activity = result.getData();
        if (flag == false) {//商品为折扣商品 无法参与满减 并按照折扣价格计算
            activity = null;//把活动置空
        }

        //循环设置图片
        Map<String, String> picUrl = fileClient.getPicUrl(imges);
        for (Cart cart : carts) {
            cart.setImage(picUrl.get(cart.getImage()));
        }
        /**
         * 没有满减活动--> 计算方式
         */
        if (Objects.isNull(activity)){
            cartDTO.setCartList(carts);
            cartDTO.setSumGoods(sumGoods);//商品总数量
            cartDTO.setSumPrice(sumPrice);//商品总价
            cartDTO.setSubtractPrice(BigDecimal.ZERO);//满减为0
            // 红包金额大于本次购买商品总价
            if(sumPrice.subtract(BigDecimal.valueOf(redpackageMoney)).compareTo(BigDecimal.ZERO) == -1){
                cartDTO.setSumPriceShow(BigDecimal.ZERO);//最终价格为0
            }else {
                //有门槛红包金额  再减去红包金额
                cartDTO.setSumPriceShow(sumPrice.subtract(BigDecimal.valueOf(redpackageMoney)));
        }
            return cartDTO;
        }else {
            /**
             * 有满减活动--> 计算方式
             */
            Integer reduceNum = activity.getReduceNum();//满减活动 减免了多少钱
            BigDecimal subtractPrice = new BigDecimal(String.valueOf(sumPrice.subtract(BigDecimal.valueOf(reduceNum))));//满减之后 价格
            cartDTO.setCartList(carts);
            cartDTO.setSumGoods(sumGoods);//商品总数量
            cartDTO.setSumPrice(sumPrice);//商品总价
            cartDTO.setSubtractPrice(new BigDecimal(reduceNum));//展示 满减活动 减免了多少钱
            //红包金额大于本次购买商品总价
            int i = new BigDecimal(redpackageMoney).compareTo(subtractPrice);
            if(i==1){
                cartDTO.setSumPriceShow(new BigDecimal(0));
            }
            //有门槛红包金额  再减去红包金额
            cartDTO.setSumPriceShow(subtractPrice.subtract(BigDecimal.valueOf(redpackageMoney)));//-(满减+红包) 之后总价格
            return cartDTO;
        }
    }



    /**
     * 修改购物车数量
     * @param cart
     */
    @Transactional
    public void updateCartNum(Cart cart) {
        //1.获取用户信息
        UserEntity userEntity = getUserEntity();
        String userUuid = userEntity.getUid();
        String goodsUuid = cart.getGoodsUuid();
        String shopUuid = cart.getShopUuid();
        Integer num = cart.getNum();
        CartMapper.update(cart,new UpdateWrapper<Cart>().eq("user_uuid",userUuid).eq("goods_uuid",goodsUuid).eq("shop_uuid",shopUuid).set("num",num));
    }

    /**
     * 根据UUID删除购物车一个商品
     *
     */
    @Transactional
    public void deleteCart(String cartUuid) {
        CartMapper.delete(new QueryWrapper<Cart>().eq("uuid",cartUuid));
    }

    /**
     * 清空购物车
     * @param shopUuid
     */
    @Transactional
    public void  clearCart(String shopUuid){
        UserEntity userEntity = getUserEntity();
        String userUuid = userEntity.getUid();
        List<Cart> list = CartMapper.selectList(new QueryWrapper<Cart>().eq("user_uuid", userUuid).eq("shop_uuid", shopUuid));
        ArrayList<Long> ids = new ArrayList();
        for (Cart cart : list) {
            ids.add(cart.getId());
        }
        CartMapper.deleteBatchIds(ids);
    }

    /**
     * 批量删除购物车
     * @param cartIds
     */
    @Transactional
    public void  BatchDeleteCart(String cartIds){
        String[] ids = cartIds.split(",");
        for (String id : ids) {
            this.deleteCart(id);
        }
    }

    /**
     * 累减购物车
     * @param cartUuid
     */
    @Override
    @Transactional
    public void subtractNum(String cartUuid) {
        Cart cart = CartMapper.selectOne(new QueryWrapper<Cart>().eq("uuid", cartUuid));
        Integer num = cart.getNum();
        num--;
        CartMapper.update(cart,new UpdateWrapper<Cart>().eq("uuid",cartUuid).set("num",num));
        if(num<=0){
            this.deleteCart(cartUuid);
        }
    }
    /**
     * 查询购物车 2.0
     * 结合满减活动、红包、折扣商品、秒杀商品、新客（首单立减）
     * 把用户参加过的活动进行统计
     */
    @Override
    public CartDTO queryCart2(CartQuery cartQuery) {
        UserEntity userEntity = getUserEntity();
        String userUuid = userEntity.getUid();
        String shopUuid = cartQuery.getShopUuid();

        //当前系统时间
        long localTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));

        //商品实付总价
        BigDecimal sumPrice = BigDecimal.ZERO;

        //商品总数量
        Integer sumGoods=0;

        //商品实付总价 前端展示
        BigDecimal sumPriceShow=BigDecimal.ZERO;//0

        //商品共优惠？
        BigDecimal subtractPrice=BigDecimal.ZERO;//0

        //购物车篮子
        List<Cart> cartList=new ArrayList<>();

        //活动Map 红包金额+满减金额+新客立减金额
        HashMap<String, BigDecimal> HashMap = new HashMap<>();

        //红包金额
        BigDecimal redpackageMoney =BigDecimal.ZERO;

        //满减金额
        BigDecimal activityMoney=BigDecimal.ZERO;

        //新客立减金额
        BigDecimal newUserMoeny=BigDecimal.ZERO;

        //其他费用（包含打包费）->以后可能会添加其他费用
        BigDecimal otherMoney=BigDecimal.ZERO;

        //配送费
        BigDecimal distributionMoeny=BigDecimal.ZERO;

        //返回对象
        CartDTO cartDTO = new CartDTO();

        //查询某一个用户在某一个店 购物车列表
        List<Cart> carts = CartMapper.selectList(new QueryWrapper<Cart>().eq("user_uuid",userUuid).eq("shop_uuid", shopUuid));cartList=carts;

        //商品总数
        sumGoods = carts.stream().mapToInt(Cart::getNum).sum();

        //是否是新客
        String isNewUserUuid = shopClient.isNewUser(shopUuid, userUuid).getData();

        //新客立减活动
        NewUser newUser = shopClient.getNewUser(shopUuid).getData();

        //查询秒杀时间表
        Timetable timetable = timeTableClient.selectTimeLong().getData();

        /**********************************************************/
        /**进行新的结算方案：折扣商品->新客立减->满减活动->优惠券(红包)***/
        /**********************************************************/

        ArrayList<SelectGoodsOtherCostByGoodsUuidParam> goodsUuidList = new ArrayList<>();//商品uuidList
        //折扣、秒杀、普通->计算单价*数量
        for (Cart cart : carts) {
            SelectGoodsOtherCostByGoodsUuidParam selectGoodsOtherCostByGoodsUuidParam = new SelectGoodsOtherCostByGoodsUuidParam();
            selectGoodsOtherCostByGoodsUuidParam.setGoodUuid(cart.getGoodsUuid());
            selectGoodsOtherCostByGoodsUuidParam.setNum(cart.getNum());
            goodsUuidList.add(selectGoodsOtherCostByGoodsUuidParam);
            //普通商品类
            if (cart.getKillGoods()==0) {
                //折扣活动状态(1启用0未启动)
                if (cart.getActivityStatus()==1){
                    if (localTime>=cart.getDiscountStartTime().toEpochSecond(ZoneOffset.of("+8"))
                            && localTime<=cart.getDiscountEndTime().toEpochSecond(ZoneOffset.of("+8"))){
                        if(cart.getDiscountStatus()==1){//打折方式
                            BigDecimal price1 = cart.getPrice().multiply(BigDecimal.valueOf(cart.getDiscount()));//商品折后价
                            //购物车里面每行商品 折扣单价*数量
                            cart.setSumCartPriceShow(price1.multiply(BigDecimal.valueOf(cart.getNum())));
                            //折扣价*数量
                            sumPrice=sumPrice.add(price1.multiply(BigDecimal.valueOf(cart.getNum())));
                            continue;
                        }else if (cart.getDiscountStatus()==0){//折扣价方式
                            BigDecimal price2 = cart.getDiscountPrice();//商品折后价
                            //购物车里面每行商品 折扣单价*数量
                            cart.setSumCartPriceShow(price2.multiply(BigDecimal.valueOf(cart.getNum())));
                            //折扣价*数量
                            sumPrice=sumPrice.add(price2.multiply(BigDecimal.valueOf(cart.getNum())));
                            continue;
                        }
                    }
                }
                //购物车里面每行商品 单价*数量
                cart.setSumCartPriceShow(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNum())));
                //原价*数量
                sumPrice=sumPrice.add(cart.getPrice().multiply(BigDecimal.valueOf(cart.getNum())));

            }

            //秒杀商品类
            if (cart.getKillGoods()==1){
                //购物车里面每行商品 秒杀单价*数量
                cart.setSumCartPriceShow(cart.getKillPrice().multiply(BigDecimal.valueOf(cart.getNum())));
                //秒杀单价*数量
                sumPrice=sumPrice.add(cart.getKillPrice().multiply(BigDecimal.valueOf(cart.getNum())));
                cartDTO.setKillGoods(1);
                if (Objects.nonNull(timetable)){//时间不为空
                    LocalDateTime killendTime = timetable.getEndTimeLong();
                    if(killendTime.isBefore(LocalDateTime.now())){//活动结束时间没有过
                        cart.setKillState(1);//当前时间已过活动结束时间 该秒杀商品失效
                    }else{
                        cart.setKillState(0);//未过时有效   ps://前端根据cart状态0,1进行判断
                    }
                }
            }
        }

        //新客立减
        if (StringUtils.isNotEmpty(isNewUserUuid)){
            //店中有活动
            if (Objects.nonNull(newUser)){
                newUserMoeny=newUserMoeny.add(newUser.getPrice());
                sumPrice=sumPrice.subtract(newUserMoeny);
                subtractPrice=subtractPrice.add(newUserMoeny);
                if (sumGoods!=0){
                    HashMap.put("新客立减",newUserMoeny);
                }
            }
        }

        //查询是否有满足价格的活动
        Activity activity = activityClient.findOne(shopUuid, sumPrice).getData();

        //满减活动
        if (Objects.nonNull(activity)){
            activityMoney=activityMoney.add(BigDecimal.valueOf(activity.getReduceNum()));
            sumPrice=sumPrice.subtract(activityMoney);
            subtractPrice=subtractPrice.add(activityMoney);
            HashMap.put("满减活动",activityMoney);
        }

        //用户使用红包 ps:一定是传入有效的红包
        String redPackageUuid = cartQuery.getRedPackageUuid();
        if (StringUtils.isNotEmpty(redPackageUuid)){
            //查询红包的价格
            UserRedpacket userRedpacket = UserRedpackClient.get(redPackageUuid).getData();
            if (Objects.isNull(userRedpacket)){
                throw new JSYException(-1,"红包参数异常!");
            }
            redpackageMoney=redpackageMoney.add(BigDecimal.valueOf(userRedpacket.getMoney()));
            sumPrice=sumPrice.subtract(redpackageMoney);
            subtractPrice=subtractPrice.add(redpackageMoney);
            HashMap.put("红包抵扣",redpackageMoney);
        }

        //需要展示给前端的其他费用（包含打包费）
        List<SelectGoodsOtherCostByGoodsUuidDto> dataList = goodsOtherCostController.selectGoodsOtherCostByGoodsUuid(goodsUuidList).getData();
        if (dataList.size()!=0){
            //其他费用（包含打包费）
            BigDecimal reOtherMoney = dataList.stream().map(x->x.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add);otherMoney=reOtherMoney;//其他费用里面的price累加
        }

        
        //配送费计算->前端展示另需配送多少》自行增减
        BigDecimal reDistributionMoney = ShopInfoClient.selectPostage(shopUuid).getData(); distributionMoeny=reDistributionMoney;

        sumPrice=sumPrice.add(otherMoney).add(distributionMoeny);

        //防止红包大于总价问题
        sumPriceShow=sumPrice.compareTo(BigDecimal.ZERO)<0?BigDecimal.ZERO:sumPrice;

        cartDTO.setHashMap(HashMap);//活动Map

        cartDTO.setSumPrice(sumPrice);//后端逻辑->实付

        cartDTO.setSumPriceShow(sumPriceShow);//前端展示->实付

        cartDTO.setSubtractPrice(subtractPrice);//共优惠？

        cartDTO.setCartList(cartList);//购物车篮子

        cartDTO.setSumGoods(sumGoods);//商品总数量

        cartDTO.setOtherMoney(dataList);//展示给前端的其他费用

        cartDTO.setDistributionMoney(distributionMoeny);//配送费

        return cartDTO;
    }

    @Override
    public List<ShopInfo> getShopList(List<String> collect1) {
        return CartMapper.getShopList(collect1);
    }

}


