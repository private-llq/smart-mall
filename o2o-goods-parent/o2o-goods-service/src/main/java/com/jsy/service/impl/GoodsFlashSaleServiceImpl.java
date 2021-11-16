package com.jsy.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jsy.basic.util.utils.CurrentUserHolder;
import com.jsy.basic.util.utils.UUIDUtils;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.config.FlashSaleRabbitConfig;
import com.jsy.domain.GoodsBasic;
import com.jsy.handle.MessageHandle;
import com.jsy.mapper.GoodsBasicMapper;
import com.jsy.service.IGoodsFlashSaleService;
import com.jsy.vo.RecordVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
@Service
@Slf4j
public class GoodsFlashSaleServiceImpl extends ServiceImpl<GoodsBasicMapper, GoodsBasic> implements IGoodsFlashSaleService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private GoodsBasicServiceImpl GoodsBasicServiceImpl;

    @Autowired
    private RecordServiceImpl RecordServiceImpl;

    @Autowired
    private RedisTemplate redisTemplate;

//    @Autowired
//    private OrderClient orderClient;

    @Autowired
    private CartServiceImpl CartServiceImpl;

    /*private  long userUuid=0;//后面改成string*/

    //开始抢单
    @Override
    public String RushPurchase(String goodsUuid, String shopUuid) {
       /* userUuid++;*/
        //同一用户的UUid作为key，相同的key进来，返回第二个key
        UserDto currentUser = CurrentUserHolder.getCurrentUser();
        String userUuid = currentUser.getUuid();
        if (this.checkSeckillUser(userUuid)==false){
                return "请勿重复抢购！";
            }
            //抢的时候先查询库存
            GoodsBasic goods = GoodsBasicServiceImpl.getOne(new QueryWrapper<GoodsBasic>().eq("uuid",goodsUuid).eq("shop_uuid",shopUuid));

            if (goods!=null&&goods.getStock()>0){
                try {
                    this.send(new MessageHandle(true, userUuid, goodsUuid, shopUuid));
                    return "恭喜你！抢购成功！";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "系统繁忙，请重试！";
                }
            }else {
                return "库存不足！一抢而空！";
            }
        }



    public boolean checkSeckillUser(String userUuid){
        String key = "goods:"+userUuid;
        return redisTemplate.opsForValue().setIfAbsent(key,"1");
    }

    //发送消息到MQ
    public String send(MessageHandle message){
        //第一个参数：交换机名字  第二个参数：Routing Key的值  第三个参数：传递的消息对象
        CorrelationData correlationData= new CorrelationData(message.getUserUuid());
        rabbitTemplate.convertAndSend(FlashSaleRabbitConfig.DIRECT_EXCHANGE, FlashSaleRabbitConfig.DIRECT_KEY, message,correlationData);
        return "发送消息";
    }

    //消费MQ消息的方法
    //被监听,有消息就调用这个方法消费，如果库存不足了，就抛出异常，监听器监听到异常，mq就拒绝其他消息进入;
    public void robbingProduct(String userUuid,String goodsUuid,String shopUuid) {

        GoodsBasic goods = GoodsBasicServiceImpl.getOne(new QueryWrapper<GoodsBasic>().eq("uuid",goodsUuid).eq("shop_uuid",shopUuid));
        if (goods != null && goods.getStock() > 0) {
            //更新库存表，库存量减少1。返回1说明更新成功。返回0说明库存已经为0
            int i = GoodsBasicServiceImpl.updateGoods(goodsUuid);
            if(i>0){
                /*//先查询数据库，MQ作了限流10，每次消费10条消息
                Record user = RecordServiceImpl.getOne(new QueryWrapper<Record>().eq("user_Uuid", userUuid));
                if (!Objects.isNull(user)){
                    //在把库存还回去
                    GoodsBasicServiceImpl.updateGoodsReturn(goodsUuid);
                    LOGGER.error("用户{}重复抢单", userUuid);
                    return;
                }*/
                //调订单接口
                /*OrderVo orderVo = new OrderVo();
                orderVo.setShopGoodsIds(goodsUuid+":"+1);
                orderVo.setUserUuid(userUuid);
                orderVo.setShopUuid(shopUuid);
                orderClient.saveOrder(orderVo);*/

                //插入记录
                GoodsBasicServiceImpl.insertProductRecord(new RecordVo(null, UUIDUtils.getUUID(),goodsUuid,userUuid,shopUuid));
                //发送短信
                log.info("用户{}抢单成功", userUuid);
            }else {
                log.error("库存已抢购一空，用户{}抢单失败", userUuid);
            }
        } else {
             log.error("库存不足，用户{}抢单失败", userUuid);
        }
    }



}
