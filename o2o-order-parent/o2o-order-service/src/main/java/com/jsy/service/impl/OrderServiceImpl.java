package com.jsy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jsy.annotation.ServiceAnnotation;
import com.jsy.basic.util.LocalTimeUtil;
import com.jsy.basic.util.RedisStateCache;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.ObjectMapperUtil;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.basic.util.vo.UserEntity;
import com.jsy.client.*;
import com.jsy.domain.*;
import com.jsy.dto.*;
import com.jsy.mapper.OrderMapper;
import com.jsy.query.CartQuery;
import com.jsy.service.IOrderCommodityService;
import com.jsy.service.IOrderService;
import com.jsy.service.IShopAssetsService;
import com.jsy.service.IShopRecordService;
import com.jsy.vo.OrderVo;
import com.jsy.vo.ShopRecordVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IOrderCommodityService orderCommodityService;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IShopRecordService shopRecordService;

    @Autowired
    private IShopAssetsService shopAssetsService;

    @Autowired
    private RedisStateCache redisStateCache;

    @Resource
    private ShopClient shopClient;

    @Autowired
    private ActivityClient activityClient;

    @Autowired
    private UserAddressClient userAddressClient;

    @Autowired
    private ShopInfoClient shopInfoClient;

    @Autowired
    private CartClient cartClient;

    @Autowired
    private ShopAppSeckillClient shopAppSeckillClient;

    private static final String PERFIX= "Order:";
    /*
    @Override
    @Transactional
    public void save(OrderVo orderVo) {
        //设置未支付
        orderVo.setStateId(1);
        orderVo.setEvaluationId(-1);
        orderVo.setCreateTime(LocalDateTime.now());
        /* 处理商品订单表,将前段传来的订单商品ids拆成id，并且给订单商品表插入商品;
           前段将商品，商品数量做处理[A:a, B:b]    A、B是商品id    a、b是商品数量
        String shopGoodsIds = orderVo.getShopGoodsIds();
        Order order = new Order();
        BeanUtils.copyProperties(orderVo, order);
        order.setUuid(UUID.randomUUID().toString());
        orderMapper.insert(order);
        String orderUuid = order.getUuid();
        orderCommodityService.getCommoditysByOid(shopGoodsIds, orderUuid);
        //orderDistributionService.insertByParams(userAddress, user, orderId);
    }*/


    @Override
    public int deleteById(String uuid) {
        /* 删除该订单的订单地址表 */
        //orderDistributionService.delByOrderId(id);
        return orderMapper.deleteGoodsById(uuid);
    }

    @Override
    public List<OrderCommodityDto> findGoods(String uuid) {

        return orderCommodityService.toListDto(orderMapper.getCommodity(uuid));
    }

    @Override
    public List<OrderDto> selectWait(String uuid) {
        return toListDto(orderMapper.selectWait(uuid));
    }

    @Override
    public List<OrderDto> selectBack(String uuid) {
        return toListDto(orderMapper.selectBack(uuid));
    }

    @Override
    public List<OrderDto> getHistory(String uuid) {
        List<Order> history = orderMapper.getHistory(uuid);
        return toListDto(history);
    }

    @Override
    public List<OrderDto> getByUid(String userUuid, String uuid) {
        List<Order> orderList = orderMapper.getByUid(userUuid, uuid);

        return toListDto(orderList);
    }

    @Override
    public List<OrderDto> getBillBySid(String uuid) {
        List<Order> list = orderMapper.getBillBySid(uuid);
        List listDto = orderService.toListDto(list);
        return listDto;
    }

    @Override
    public int changeStatus(String uuid) {
        return orderMapper.changeStatus(uuid);
    }

    @Override
    public int deleteByStatus() {
        return orderMapper.deleteByStatus();
    }

    @Override
    public List<OrderInfoDto> getByUserUuid(String uuid) {
        return orderMapper.getByUserUuid(uuid);
    }

    @Override
    public int finishOrderByUuid(String uuid) {
        return orderMapper.finishOrderByUuid(uuid);
    }


    @Override
    public int deleteOrderByUuid(String uuid) {
        return orderMapper.deleteOrderByUuid(uuid);
    }

    @Override
    public List<OrderDto> backOrderByUuid(String uuid) {

        return toListDto(orderMapper.backOrderByUuid(uuid));
    }

    @Override
    public OrderDto getByUuid(String uuid) {
        Order order = orderMapper.getByUuid(uuid);

        if (!Objects.isNull(order)){
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(order,orderDto);
            return orderDto;
        }
       return null;
    }

    @Override
    public BigDecimal getMoney(QueryWrapper<Order> queryWrapper) {
        return orderMapper.getMoney(queryWrapper);
    }

    @Override
    public List<JSONObject> getRedpacket(QueryWrapper<Order> queryWrapper) {
        return orderMapper.getRedpacket(queryWrapper);
    }

    @Override
    public int updateEid(String orderUuid) {
        return orderMapper.updateEid(orderUuid);
    }

    @ServiceAnnotation(description = "save")
    @Override
    public Order save(OrderVo ordervo, UserAddress userAddress, CartDTO cartDTO, String uuid) {

        //订单对某次活动的首单做记录
        String isNewUserUuid = shopClient.isNewUser(ordervo.getShopUuid(), ordervo.getUserUuid()).getData();
        List<Cart> cartList = cartDTO.getCartList();
        Order order = newOrder(ordervo, userAddress, cartDTO);
        if (StringUtils.isNotEmpty(isNewUserUuid)){//判断是否新客（首单）
            order.setNewUserUuid(isNewUserUuid);
        }else {
            order.setNewUserUuid("0");
        }
        //TODO：未支付
        order.setPayState("0");
        this.save(order);
        //redisStateCache.cache(perfix+order.getUuid(),order.toString(),30*60);
        //redisStateCache.cache(Order+":"+ UUIDUtils.getUUID(),order.toString(),20);
        ShopRecordVo shopRecordVo = new ShopRecordVo();
        shopRecordVo.setRecord(order.getOrderPrice());
        shopRecordVo.setTurnType(1);
        shopRecordVo.setUuid(uuid);
        shopRecordVo.setOrderUuid(order.getUuid());
        shopRecordService.save(shopRecordVo);
        //TODO：订单完成之后再加金额
        //shopAssetsService.updateAssets(1,order.getOrderPrice(),ordervo.getShopUuid());
        orderCommodityService.saveByCart(cartList,order.getUuid());

        //为抢购商品，库存减少订单上该商品的数量
        if (cartDTO.getKillGoods()==1){
            // 过滤：为秒杀商品
            cartList.stream().filter(x->{
                return x.getKillGoods()==1;
            }).collect(Collectors.toList());
            for (Cart cart : cartList) {
                Integer cartNum = cart.getNum();
                String cartGoodsUuid = cart.getGoodsUuid();
                String shopUuid = cart.getShopUuid();
                //调用 接口减少库存
                shopAppSeckillClient.updateStock(shopUuid,cartGoodsUuid,cartNum);
            }
        }

        return this.getOne(new QueryWrapper<Order>().eq("uuid", order.getUuid()));
    }

    @Override
    public int generateCode(OrderDto orderDto) {
        if (Objects.isNull(orderDto)){
            return 0;
        }
        Order order = new Order();
        BeanUtil.copyProperties(orderDto,order);
        return this.update(order,new QueryWrapper<Order>().eq("uuid",order.getUuid()))? 1:0;
    }

    @Override
    public int changeUsed(String serviceCode,String uuid) {
        if (StringUtils.isEmpty(serviceCode)){
            return 0;
        }
        Order order = this.getOne(new QueryWrapper<Order>().eq("uuid", uuid));

        if (serviceCode.equals(order.getServiceCode())){
            return orderMapper.changeUsed(uuid);
        }
        return 0;
    }

    @Override
    public int noCancelOrder(String uuid) {
        if (StringUtils.isBlank(uuid)){
            return 0;
        }
        return orderMapper.update(null,new UpdateWrapper<Order>().eq("uuid",uuid).set("pay_state",4));
    }

    @Override
    public List<OrderDto> waitPayed(String uid) {
        if (StringUtils.isBlank(uid)){
            return null;
        }
        List<Order> list = this.list(new QueryWrapper<Order>().eq("pay_state","0").eq("user_uuid", uid));
        return toListDto(list);
    }

    @Override
    public List<OrderDto> waitUsed(String uid) {
        //服务类
        List<Order> list = this.list(new QueryWrapper<Order>().eq("user_uuid", uid).eq("pay_state",1).eq("used", 0));
        return toListDto(list);
    }

    @Override
    public List<OrderDto> ownOrder(String uid) {
        List<Order> list = this.list(new QueryWrapper<Order>().eq("state_id", 3));
        return toListDto(list);
    }

    @Override
    public Map<String,String> getTurnover(QueryWrapper<Order> queryWrapper) {
        return orderMapper.getTurnover(queryWrapper);
    }

    @Override
    public int selectBackCount(String uid) {
        return orderMapper.selectBackCount(uid);
    }

    @Override
    public int acceptOrder(String uuid, String uid) {
        return this.update(null,new UpdateWrapper<Order>().eq(StringUtils.isNotBlank(uuid),"uuid",uuid).eq(StringUtils.isNotBlank(uid),"user_uuid",uid).set("state_id",1))?1:0;
    }

    @Override
    public IPage<OrderInfoDto> pageUserState(Page page, QueryWrapper<Order> queryWrapper) {
        return orderMapper.pageUserState(page,queryWrapper);
    }

    @Override
    public List<OrderInfoDto> getRunOrder(String uuid) {
        return orderMapper.getRunOrder(uuid);
    }

    @Override
    public int setRunOrder(String uuid) {
        return orderMapper.setRunOrder(uuid);
    }

    @Override
    public int cancelOrder(String uuid) {
        return orderMapper.cancelOrder(uuid);
    }

    @Override
    public int cancelOrderList(List<String> strings) {
        return orderMapper.cancelOrderList(strings);
    }

    @Override
    public List<OrderDto> backOrderBySUuid(String uuid) {
        return orderMapper.backOrderBySUuid(uuid);
    }

    @Override
    public int finishCancelOrder(String uuid) {

        Order order = orderMapper.getByUuid(uuid);
        UserDto user = CurrentUserHolder.getCurrentUser();
        ShopAssets shopAssets = shopAssetsService.getByUUid(user.getUuid());
        shopAssets.setProfits(shopAssets.getProfits().subtract(order.getOrderPrice()));
        shopAssetsService.update(shopAssets,new QueryWrapper<ShopAssets>().eq("uuid",shopAssets.getUuid()));

        return orderMapper.finishCancelOrder(uuid);
    }
    @Override
    public int payedOrder(String uuid) {
        //TODO:订单支付成功需要调用社区的支付模块并更改订单状态，并且删除在redis中保存的未支付订单，利用队列同步到数据库
        //1、调用社区支付模块

        //2、删除redis中的保存的未支付订单，并放到队列

        return orderMapper.payedOrder(uuid);
    }

    @Override
    public Order saveOrder(OrderVo orderVo) {
        UserEntity userEntity = CurrentUserHolder.getUserEntity();

        orderVo.setUserUuid(userEntity.getUid());
        //转实体类
        ObjectMapper mapper=new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        //获取用户地址
        CommonResult addressResult = userAddressClient.getByUuid(orderVo.getAddressUuid());
        if (Objects.isNull(addressResult)){
            throw new JSYException(-1,"请填写送货地址");
        }

        UserAddress userAddress = mapper.convertValue(addressResult.getData(), UserAddress.class);
        //购物车
        CartQuery cartQuery = new CartQuery();
        cartQuery.setShopUuid(orderVo.getShopUuid());
        cartQuery.setUserUuid(userEntity.getUid());

        if (StringUtils.isNotEmpty(orderVo.getUserRedpacket())){
            cartQuery.setRedPackageUuid(orderVo.getUserRedpacket());
        }
        CommonResult cartDtoCommonResult = cartClient.queryCart2(cartQuery);
        if (cartDtoCommonResult.getCode()==-1){
            throw new  JSYException(-1,"下单失败，获取购物车失败");
        }

        CartDTO cartDTO = mapper.convertValue(cartDtoCommonResult.getData(), CartDTO.class);
        if (cartDTO.getCartList().size()==0){
            throw new  JSYException(-1,"下单失败,购物车为空");
        }

        CommonResult<ShopMessageDto> result = shopInfoClient.selectShopMessage(orderVo.getShopUuid());

        if(Objects.isNull(result.getData())){
            throw new JSYException(-1,"店铺已注销");
        }
        ShopMessageDto shopMessageDto = result.getData();

        //店铺拥有者的uuid
        String uuid = shopMessageDto.getOwnerUuid();

        CommonResult<ShopInfo> commonResult = shopInfoClient.get(orderVo.getShopUuid());
        if (commonResult.getCode()!=0){
            throw new  JSYException(-1,"商铺不存在");
        }
        ShopInfo shopInfo = commonResult.getData();
        //生成订单编号
        orderVo.setOrderNum(UUIDUtils.getOrderNum()+shopInfo.getShopNumber().substring(shopInfo.getShopNumber().length()-4));

        return this.save(orderVo, userAddress, cartDTO, uuid);
    }

    @Override
    public int changeStatusByUid(Integer stateId, String uuid) {
        return orderMapper.changeStatusByUid(stateId,uuid);
    }

    @Override
    public List<Order> getByStateId(String stateId) {
        return orderMapper.getByStateId(stateId);
    }

    @Override
    public List<Order> billDetails() {
        UserDto user = CurrentUserHolder.getCurrentUser();
        String uuid = user.getUuid();
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(StringUtils.isNotBlank(uuid),"shop_uuid",uuid)
                .ge("create_time",LocalTimeUtil.getStart(0))
                .le("create_time",LocalTimeUtil.getEnd(0))
                .eq("pay_state",1);
        return this.baseMapper.billDetails(queryWrapper);
    }

    @Override
    public OrderDto selectByOrderNum(String orderNum) {
        OrderDto orderDto = new OrderDto();
        Order order = this.getOne(new QueryWrapper<Order>().eq("order_num",orderNum));
        BeanUtils.copyProperties(order,orderDto);
        return orderDto;
    }

   // 统计累计新客 -1：昨日新客 0：今日新客 1:累计新客 2：累计成本  "
    @Override
    public NewUserDto statisticsNewUser(String shopUuid) {
        long localTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
        CommonResult<NewUser> data = shopClient.newestNewUser(shopUuid);
        ObjectMapperUtil.insObject(data, NewUser.class);//实例化
        NewUser newUser = data.getData();//最新新客立减活动
        String userUuid = newUser.getUuid();

        long startTime = newUser.getStartTime().toEpochSecond(ZoneOffset.of("+8"));//活动结束时间
        long endTime = newUser.getEndTime().toEpochSecond(ZoneOffset.of("+8"));//活动开始时间
        /**
         * 累计新客
         */
        List<Order> collectList = orderService.list(new QueryWrapper<Order>().eq("shop_uuid", shopUuid).eq("new_user_uuid", userUuid).notIn("new_user_uuid","0"));
        /*List<Order> collectList = list.stream().filter(x -> {//每次都获取最新一批新客
            if (newUser.getUuid().equals(x.getNewUserUuid())) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());*/

        /**
         * 累计成本
         */
        BigDecimal price = newUser.getPrice().multiply(BigDecimal.valueOf(collectList.size()));

        /**
         * 昨日新客
         */
        long startOfDay = LocalTimeUtil.getStartOfDay(LocalDateTime.now().plusDays(-1));
        long endOfDay = LocalTimeUtil.getEndOfDay(LocalDateTime.now().plusDays(-1));
        List<Order> collect = collectList.stream().filter(x -> {
            if (x.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() >= startOfDay && x.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() <= endOfDay) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        /**
         * 今日新客
         */
        long startOfDay1 = LocalTimeUtil.getStartOfDay(LocalDateTime.now());
        long endOfDay1 = LocalTimeUtil.getEndOfDay(LocalDateTime.now());

        List<Order> collect1 = collectList.stream().filter(x -> {
            if (x.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() >= startOfDay1 && x.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() <= endOfDay1) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        Double durationDay= Double.valueOf((endTime-startTime)/ (3600*24));//持续天数
        Double surplusDay= Double.valueOf((endTime-localTime)/(3600*24));//剩余天数

        NewUserDto newUserDto = new NewUserDto();
        newUserDto.setCumulativePrice(price);//累计价格
        newUserDto.setCumulativeNewUser(collectList.size());//累计新客
        newUserDto.setTodayNewUser(collect1.size());//今日新客
        newUserDto.setYesterdayNewUse(collect.size());//昨日新客
        newUserDto.setDurationDay(durationDay<=0.5?0.5:durationDay);//持续天数
        newUserDto.setIsConduct(newUser.getState());//返回给前端的状态码 1 : 进行中 2 已撤销 3 已过期 4未开始
        newUserDto.setSurplusDay(surplusDay);//剩余天数
        newUserDto.setStartTime(startTime);//活动开始时间
        newUserDto.setEndTime(endTime);//活动结束时间
        newUserDto.setCreatTime(newUser.getCreatTime().toEpochSecond(ZoneOffset.of("+8")));//活动创建时间
        newUserDto.setRevokeTime(newUser.getRevokeTime()==null?null:newUser.getRevokeTime().toEpochSecond(ZoneOffset.of("+8")));//活动撤销时间
        newUserDto.setPrice(newUser.getPrice());//立减金额

        return newUserDto;
    }

    @Override
    public ActivityDTO statisticalActivities(String shopUuid) {
        CommonResult<List<Activity>> listCommonResult = activityClient.newestActivities(shopUuid);//正在进行中的活动
        List<Activity> activities = listCommonResult.getData();
        long localTime = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));

        long startTime = activities.get(0).getBeginTime().toEpochSecond(ZoneOffset.of("+8"));//活动开始时间
        long endTime = activities.get(0).getEndTime().toEpochSecond(ZoneOffset.of("+8"));//活动结束时间
        /**
         * 累计订单
         */
        ArrayList<String> ids = new ArrayList<>();

        activities.forEach(x->{
            ids.add(x.getUuid());
        });
        List<Order> list = orderService.list(new QueryWrapper<Order>().eq("shop_uuid", shopUuid).eq("state_id",3));

        List<Order> orders = list.stream().filter(x -> {//累计订单
            //并且这个外检是正在进行活动中的一个id
            if ("0".equals(x.getActivityUuid())){
                return false;
            }
            if (!ids.contains(x.getActivityUuid())){
                return false;
            }
            return true;
        }).collect(Collectors.toList());

        /**
         * 累计减免
         */
        Integer sumPic=0;//累计减免金额
        ArrayList<String> activitiesIds = new ArrayList<>();
        orders.forEach(x->{
            activitiesIds.add(x.getActivityUuid());

        });

        List<Activity> activityList = activityClient.ActivitiesList(activitiesIds);
        for (Activity activity : activityList) {
            sumPic+=activity.getReduceNum();
        }

        /**
         * 昨日订单
         */
        long startOfDay = LocalTimeUtil.getStartOfDay(LocalDateTime.now().plusDays(-1));
        long endOfDay = LocalTimeUtil.getEndOfDay(LocalDateTime.now().plusDays(-1));
        List<Order> collect = orders.stream().filter(x -> {
            if (x.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() >= startOfDay && x.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() <= endOfDay) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        /**
         * 今日订单
         */
        long startOfDay1 = LocalTimeUtil.getStartOfDay(LocalDateTime.now());
        long endOfDay1 = LocalTimeUtil.getEndOfDay(LocalDateTime.now());

        List<Order> collect1 = orders.stream().filter(x -> {
            if (x.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() >= startOfDay1 && x.getCreateTime().toInstant(ZoneOffset.of("+8")).toEpochMilli() <= endOfDay1) {
                return true;
            }
            return false;
        }).collect(Collectors.toList());

        Double durationDay= Double.valueOf((endTime-startTime)/ (3600*24));//持续天数
        Double surplusDay= Double.valueOf((endTime-localTime)/(3600*24));//剩余天数

        ActivityDTO activityDTO = new ActivityDTO();
        activityDTO.setStartTime(startTime);//开始时间
        activityDTO.setEndTime(startTime);//结束时间
        activityDTO.setCreatTime(activities.get(0).getCreatTime().toEpochSecond(ZoneOffset.of("+8")));//创建时间
        activityDTO.setRevokeTime(activities.get(0).getRevokeTime()==null?null:activities.get(0).getRevokeTime().toEpochSecond(ZoneOffset.of("+8")));//撤销时间
        activityDTO.setCumulativePrice(BigDecimal.valueOf(sumPic));//累计减免
        activityDTO.setDurationDay(durationDay<=0.5?0.5:durationDay);//持续天数
        activityDTO.setSurplusDay(surplusDay);//剩余天数
        activityDTO.setIsConduct(activities.get(0).getState());//返回给前端的状态码 1 : 进行中 2 已撤销 3 已过期 4未开始
        activityDTO.setCumulativeOrder(orders.size());
        activityDTO.setTodayOrder(collect.size());
        activityDTO.setYesterdayOrder(collect1.size());

        return activityDTO;
    }

    public Order newOrder(OrderVo ordervo, UserAddress userAddress, CartDTO cartDTO){

        Order order = new Order();
        order.setOtherMoney(cartDTO.getOtherMoney().toString());
        order.setUserUuid(ordervo.getUserUuid());
        order.setShopUuid(ordervo.getShopUuid());
        order.setCreateTime(LocalDateTime.now());
        order.setOrderOriginalPrice(cartDTO.getSubtractPrice().add(cartDTO.getSumPriceShow()));
        //order.setUpdateTime();
        //2020-12-15+*********
        order.setOrderNum(new SimpleDateFormat("yyyy-MM-dd").format(new Date())+"-"+(ThreadLocalRandom.current().nextInt(100*1000*1000,1000*1000*1000-1)));
        //order.setServiceTime();
        //直接接单
        order.setStateId(1);
        order.setSubtractPrice(cartDTO.getSubtractPrice());
        order.setRedpacketUuid(ordervo.getUserRedpacket());
        order.setDeliveryFee(ordervo.getDeliveryFee());
        order.setOrderPrice(cartDTO.getSumPriceShow());
        order.setEvaluationId(-1);
        //order.setShopGoodsIds();
        order.setUsed("0");
        order.setPayState("0");
        order.setOrderMessage(ordervo.getOrderMessage());
        order.setUsername(userAddress.getName());
        order.setPhone(userAddress.getPhone());
        order.setAddress(userAddress.getAddress()+userAddress.getAddressDescription());
        order.setDeliveryWay(ordervo.getDeliveryWay());
        order.setUuid(UUID.randomUUID().toString().trim().replaceAll("-", ""));
        return order;
    }
    @Override
    public OrderDto toDto(Order entity) {
        if (Objects.isNull(entity)){
            return null;
        }
        OrderDto orderDto = new OrderDto();
        BeanUtils.copyProperties(entity,orderDto);
        orderDto.setOrderCommodityDtos(orderCommodityService.toListDto(entity.getOrderCommoditys()));
        return orderDto;
    }
    @Override
    public Order toEntity(OrderDto dto) {
        if (Objects.isNull(dto)){
            return null;
        }
        Order order = new Order();
        BeanUtils.copyProperties(dto,order);
        order.setOrderCommoditys(orderCommodityService.toListEntity(dto.getOrderCommodityDtos()));
        return order;

    }

    @Override
    public List<Order> toListEntity(List<OrderDto> list) {
        if (list.isEmpty()){
            return null;
        }
        List<Order> orders = new ArrayList<Order>(list.size());
        for (OrderDto orderDto : list){
            Order order = toEntity(orderDto);
            orders.add(order);
        }
        return orders;
    }
    @Override
    public List<OrderDto> toListDto(List<Order> list) {
        if (list.isEmpty()){
            return null;
        }
        List<OrderDto> orderDtos = new ArrayList<OrderDto>(list.size());
        for (Order order : list){
            OrderDto orderDto = toDto(order);
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }
}
