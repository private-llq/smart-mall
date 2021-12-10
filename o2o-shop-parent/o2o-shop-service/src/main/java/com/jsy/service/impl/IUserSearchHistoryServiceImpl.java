package com.jsy.service.impl;

import com.jsy.service.IUserSearchHistoryService;
import com.jsy.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class IUserSearchHistoryServiceImpl implements IUserSearchHistoryService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Boolean addSearchHistoryByUserId(Long userId, String searchkey) {
        stringRedisTemplate.hasKey(searchkey);
        return null;
    }

    @Override
    public Long delSearchHistoryByUserId(Long userId, String searchkey) {
        return null;
    }

    @Override
    public List<String> getSearchHistoryByUserId(Long userId) {
        return null;
    }

    @Override
    public Boolean incrementScoreByUserId(String searchkey) {
        return null;
    }

    @Override
    public List<String> getHotList(String searchkey) {
        return null;
    }

    @Override
    public Boolean incrementScore(String searchkey) {
        return null;
    }

//    @Resource
//    private RedisUtils redisUtils;
//
//     /**
//      * @author Tian
//      * @since 2021/12/10-14:05
//      * @description 新增搜索记录
//      **/
//     @Override
//    public Boolean addSearchHistoryByUserId(Long userId,String searchkey){
//        String shistory = redisUtils.getSearchHistoryKey(userId);
//         System.out.println("进来了" );
//         Object o = redisTemplate.opsForHash().get(shistory, searchkey);
//         System.out.println("o______________"+o);
//         redisTemplate.opsForHash().put(shistory, searchkey, "1");
//
////        boolean b = redisTemplate.hasKey(shistory);
////        if (b) {
////            Object hk = redisTemplate.opsForHash().get(shistory, searchkey);
////            if (hk != null) {
////                return true;
////            }else{
////                redisTemplate.opsForHash().put(shistory, searchkey, "1");
////            }
////        }else{
////            redisTemplate.opsForHash().put(shistory, searchkey, "1");
////        }
//        return true;
//    }
//     /**
//      * @author Tian
//      * @since 2021/12/10-14:03
//      * @description 删除个人历史数据
//      **/
//     @Override
//    public Long delSearchHistoryByUserId(Long userId, String searchkey) {
//        String shistory = redisUtils.getSearchHistoryKey(userId);
//        Long aLong = redisTemplate.opsForHash().delete(shistory, searchkey);
//        System.out.println(aLong);
//        return aLong;
//    }
//     /**
//      * @author Tian
//      * @since 2021/12/10-14:03
//      * @description 获取个人历史数据列表
//      **/
//     @Override
//    public List<String> getSearchHistoryByUserId(Long userId) {
//        List<String> stringList = null;
//        String shistory = redisUtils.getSearchHistoryKey(userId);
//        boolean b = redisTemplate.hasKey(shistory);
//        if(b){
//            Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(shistory, ScanOptions.NONE);
//            while (cursor.hasNext()) {
//                Map.Entry<Object, Object> map = cursor.next();
//                String key = map.getKey().toString();
//                stringList.add(key);
//            }
//            return stringList;
//        }
//        return null;
//    }
//     /**
//      * @author Tian
//      * @since 2021/12/10-14:04
//      * @description 新增一条热词搜索记录，将用户输入的热词存储下来
//      **/
//     @Override
//    public Boolean incrementScoreByUserId(String searchkey) {
//        Long now = System.currentTimeMillis();
//        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        List<String> title = new ArrayList<>();
//        title.add(searchkey);
//        for (int i = 0, lengh = title.size(); i < lengh; i++) {
//            String tle = title.get(i);
//            try {
//                if (zSetOperations.score("title", tle) <= 0) {
//                    zSetOperations.add("title", tle, 0);
//                    valueOperations.set(tle, String.valueOf(now));
//                }
//            } catch (Exception e) {
//                zSetOperations.add("title", tle, 0);
//                valueOperations.set(tle, String.valueOf(now));
//            }
//        }
//        return true;
//    }
//
//     /**
//      * @author Tian
//      * @since 2021/12/10-14:04
//      * @description 根据searchkey搜索其相关最热的前十名 (如果searchkey为null空，则返回redis存储的前十最热词条
//      **/
//     @Override
//     public List<String> getHotList(String searchkey) {
//        String key = searchkey;
//        Long now = System.currentTimeMillis();
//        List<String> result = new ArrayList<>();
//        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        Set<String> value = zSetOperations.reverseRangeByScore("title", 0, Double.MAX_VALUE);
//        //key不为空的时候 推荐相关的最热前十名
//        if(StringUtils.isNotEmpty(searchkey)){
//            for (String val : value) {
//                if (StringUtils.containsIgnoreCase(val, key)) {
//                    if (result.size() > 9) {//只返回最热的前十名
//                        break;
//                    }
//                    Long time = Long.valueOf(valueOperations.get(val));
//                    if ((now - time) < 2592000000L) {//返回最近一个月的数据
//                        result.add(val);
//                    } else {//时间超过一个月没搜索就把这个词热度归0
//                        zSetOperations.add("title", val, 0);
//                    }
//                }
//            }
//        }else{
//            for (String val : value) {
//                if (result.size() > 9) {//只返回最热的前十名
//                    break;
//                }
//                Long time = Long.valueOf(valueOperations.get(val));
//                if ((now - time) < 2592000000L) {//返回最近一个月的数据
//                    result.add(val);
//                } else {//时间超过一个月没搜索就把这个词热度归0
//                    zSetOperations.add("title", val, 0);
//                }
//            }
//        }
//        return result;
//    }
//
//     /**
//      * @author Tian
//      * @since 2021/12/10-14:04
//      * @description 每次点击给相关词searchkey热度 +1
//      **/
//     @Override
//    public Boolean incrementScore(String searchkey) {
//        String key = searchkey;
//        Long now = System.currentTimeMillis();
//        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
//        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
//        zSetOperations.incrementScore("title", key, 1);
//        valueOperations.getAndSet(key, String.valueOf(now));
//        return true;
//    }

}
