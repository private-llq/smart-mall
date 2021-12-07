package com.jsy.util;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.exception.JSYException;
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

}
