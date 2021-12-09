package com.jsy.util;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.client.HotClient;
import com.jsy.client.SetMenuClient;
import com.jsy.domain.HotGoods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Configuration
public class RedisUtils {
    public static final String HOT_GOODS = "hotGoods";
    private long loginExpireHour = 168;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
//    @Autowired
//    private HotClient hotClient;


    //设置C端热门商品数据  7天
    public void setHotGoods(List<HotGoods> hotGoodsList) {
        System.out.println("进来"+hotGoodsList);
        stringRedisTemplate.opsForValue().set("hotGoods", JSON.toJSONString(hotGoodsList),loginExpireHour,TimeUnit.HOURS);
    }

    public List<HotGoods> getHotGoods() {
        String str = null;
        try {
            str = stringRedisTemplate.opsForValue().get("hotGoods");
            System.out.println("str" + str);
        } catch (Exception e) {
            throw new JSYException(JSYError.INTERNAL.getCode(), "redis超时");
        }
        List<HotGoods>  hotGoodsList=JSONObject.parseArray(str, HotGoods.class);
        System.out.println(hotGoodsList);
        return hotGoodsList;
    }

    public void setHotGoods(List<HotGoods> hotGoodsList, Long hour) {
        stringRedisTemplate.opsForValue().set("hotGoods", hotGoodsList.toString(), hour, TimeUnit.HOURS);
    }

//    public void setGoods(Long goodsId){
//        //获取热门数据过期时间
//        Long time = stringRedisTemplate.getExpire("hotGoods", TimeUnit.HOURS);
//        System.out.println("剩余时间"+time);
//        HotGoods hotGoods = hotClient.getHotGoods(goodsId).getData();
//        if (hotGoods!=null){
//            //删除缓存
//            stringRedisTemplate.delete("hootGoods");
//            //去掉热门数据里商品的数据
//            hotClient.delHotGoods(goodsId);
//            //查询最新的热门数据
//            List<HotGoods> hotGoodsList = hotClient.getHotList().getData();
//            //设置新的缓存热门数据
//            setHotGoods(hotGoodsList,time);
//        }
//
//    }
//    public void setHotShop(Long shopId){
//        //获取热门数据过期时间
//        Long time = stringRedisTemplate.getExpire("hotGoods", TimeUnit.HOURS);
//        System.out.println("剩余时间"+time);
//        HotGoods hotGoods = hotClient.getHotShop(shopId).getData();
//        if (hotGoods!=null){
//            //删除缓存
//            stringRedisTemplate.delete("hootGoods");
//            //去掉热门数据里商品的数据
//            hotClient.delHotShop(shopId);
//            //查询最新的热门数据
//            List<HotGoods> hotGoodsList = hotClient.getHotList().getData();
//            //设置新的缓存热门数据
//            setHotGoods(hotGoodsList,time);
//        }
//
//    }

}
