package com.jsy.controller;


import com.jsy.basic.util.vo.CommonResult;
import com.jsy.domain.Order;
import com.jsy.domain.SetProperties;
import com.jsy.service.IOrderService;
import com.jsy.service.ISetPropertiesService;
import com.jsy.service.IShopRecordService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Component
public class ScheduledController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IShopRecordService shopRecordService;

    @Autowired
    private ISetPropertiesService setPropertiesService;

    @Autowired
    private RedisTemplate redisTemplate;

    public static final Logger log = LoggerFactory.getLogger(ScheduledController.class);

    public static final String module = "order";
    /**
     * 每隔30秒扫描订单，并删除超过30分钟未支付的订单
     * @return
     */
    //@RedisLock(key = "cancelOrder",outTime = 20,value = "1")
    //@Scheduled(cron="0/30 * * * * ?")
    public void cancelOrder(){

        //log.info("**********删除超过30分钟未支付的订单定时任务==》》开始************");
        String nopay = "30";
        try {
            //获取未支付的订单
            List<Order> list = orderService.getByStateId("0");
            List<String> strings = new ArrayList<>();
            if(list.isEmpty()){
                return;
            }
            //根据模块获取设置参数
            SetProperties setProperties = setPropertiesService.selectByModule(module);
            if (!Objects.isNull(setProperties)||StringUtils.isEmpty(setProperties.getNopay())){
                nopay = setProperties.getNopay();
            }
            for (Order order : list){
                Duration between = Duration.between(order.getCreateTime(), LocalDateTime.now());
                System.out.println(" how many ?   "+between.toMinutes());
                if (between.toMinutes()>=Integer.valueOf(nopay)){
                    strings.add(order.getUuid());
                }
            }
            if (!strings.isEmpty()){
                orderService.cancelOrderList(strings);
                shopRecordService.deleteByStr(strings);
            }
            Thread.sleep(10);
            //log.info("**********删除超过30分钟未支付的订单定时任务==》》结束************");
        } catch (Exception e){
            log.error("时间："+ LocalDateTime.now()+" 错误信息："+e.getMessage()+" ");
        } finally {

        }
    }

    public CommonResult finishEvaluation(){
        return CommonResult.ok();
    }
}
