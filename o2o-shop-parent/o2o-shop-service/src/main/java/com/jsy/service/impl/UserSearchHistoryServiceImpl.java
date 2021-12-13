package com.jsy.service.impl;

import com.jsy.service.IUserSearchHistoryService;
import com.jsy.util.RedisUtils;
import org.apache.commons.lang3.StringUtils;

import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class UserSearchHistoryServiceImpl implements IUserSearchHistoryService {

    @Resource
    private RedisUtils redisUtils;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

     /**
      * @author Tian
      * @since 2021/12/10-14:05
      * @description 新增搜索记录
      **/
     @Override
    public Boolean addSearchHistoryByUserId(Long userId,String searchkey){
        String shistory = redisUtils.getSearchHistoryKey(userId);
         System.out.println("进来了" );
         //获取当前hashmap的key
        boolean b = stringRedisTemplate.hasKey(shistory);
        if (b) {
            //获取  这个map  里面 searchkey这个key的值
            Object hk = stringRedisTemplate.opsForHash().get(shistory, searchkey);
            System.out.println("hk"+hk);
            //如果没有这个键值对
            if (hk != null) {
                return true;
            }else{
                //就往shistory 的map里面新增这个键值对   searchkey---searchkey
                stringRedisTemplate.opsForHash().put(shistory, searchkey, searchkey);
            }
        }else{
            stringRedisTemplate.opsForHash().put(shistory, searchkey, searchkey);
        }
        return true;
    }
     /**
      * @author Tian
      * @since 2021/12/10-14:03
      * @description 删除个人历史数据
      **/
     @Override
    public Long delSearchHistoryByUserId(Long userId, String searchkey) {
        String shistory = redisUtils.getSearchHistoryKey(userId);
         //删除shistory 的map里面searchkey这个键   searchkey---searchkey
        Long aLong = stringRedisTemplate.opsForZSet().remove(shistory, searchkey);
        System.out.println(aLong);
        return aLong;
    }
     /**
      * @author Tian
      * @since 2021/12/10-14:03
      * @description 获取个人历史数据列表
      **/
     @Override
    public List<String> getSearchHistoryByUserId(Long userId) {
        List<String> stringList = new ArrayList<>();
        String shistory = redisUtils.getSearchHistoryKey(userId);
        boolean b = stringRedisTemplate.hasKey(shistory);
         System.out.println("获取个人历史数据列表" +b);
        if(b){
            //匹配获取键值对，ScanOptions.NONE为获取全部键对，ScanOptions.scanOptions().match("map1").build()     匹配获取键位map1的键值对,不能模糊匹配。
            Cursor<Map.Entry<Object, Object>> cursor = stringRedisTemplate.opsForHash().scan(shistory, ScanOptions.NONE);
            while (cursor.hasNext()) {
                Map.Entry<Object, Object> map = cursor.next();
                //获取键值对  的 key  key就是搜索的词
                String key = map.getKey().toString();
                System.out.println("key"+key);
                stringList.add(key);
            }
            return stringList;
        }
        return null;
    }
     /**
      * @author Tian
      * @since 2021/12/10-14:04
      * @description 新增一条热词搜索记录，将用户输入的热词存储下来
      **/
     @Override
    public Boolean incrementScoreByUserId(String searchkey) {
         //获取当前时间戳
        Long now = System.currentTimeMillis();
        List<String> title = new ArrayList<>();
        title.add(searchkey);
        for (int i = 0, lengh = title.size(); i < lengh; i++) {
            String tle = title.get(i);
            try {
                //获取元素的分值。 如果为null证明title这个集合里面没有 tle(搜索词)
                if (stringRedisTemplate.opsForZSet().score("title", tle) <= 0) {
                    //把 tle搜索词  新增到title这个集合里面  分数为0
                    stringRedisTemplate.opsForZSet().add("title", tle, 0);
                  //搜索词为键  时间戳为值  单纯存入缓存
                    stringRedisTemplate.opsForValue().set(tle, String.valueOf(now));
                }
            } catch (Exception e) {
                stringRedisTemplate.opsForZSet().add("title", tle, 0);
                stringRedisTemplate.opsForValue().set(tle, String.valueOf(now));
            }
        }
        return true;
    }

     /**
      * @author Tian
      * @since 2021/12/10-14:04
      * @description 根据searchkey搜索其相关最热的前十名 (如果searchkey为null空，则返回redis存储的前十最热词条
      **/
     @Override
     public List<String> getHotList(String searchkey) {
        String key = searchkey;
        Long now = System.currentTimeMillis();
        List<String> result = new ArrayList<>();
        //倒序排列指定分值区间元素。  获取list集合中分数0-最大的值
        Set<String> value = stringRedisTemplate.opsForZSet().reverseRangeByScore("title", 0, Double.MAX_VALUE);
        //key不为空的时候 推荐相关的最热前十名
        if(StringUtils.isNotEmpty(searchkey)){
            for (String val : value) {
                //检查一个字符串包含另一个字符  关键词是否 在这个list中
                if (StringUtils.containsIgnoreCase(val, key)) {
                    if (result.size() > 9) {//只返回最热的前十名
                        break;
                    }
                    //获取时间戳
                    Long time = Long.valueOf(stringRedisTemplate.opsForValue().get(val));
                    if ((now - time) < 2592000000L) {//返回最近一个月的数据
                        result.add(val);
                    } else {//时间超过一个月没搜索就把这个词热度归0
                        stringRedisTemplate.opsForZSet().add("title", val, 0);
                    }
                }
            }
        }else{
            for (String val : value) {
                if (result.size() > 9) {//只返回最热的前十名
                    break;
                }
                Long time = Long.valueOf(stringRedisTemplate.opsForValue().get(val));
                if ((now - time) < 2592000000L) {//返回最近一个月的数据
                    result.add(val);
                } else {//时间超过一个月没搜索就把这个词热度归0
                    stringRedisTemplate.opsForZSet().add("title", val, 0);
                }
            }
        }
        return result;
    }

     /**
      * @author Tian
      * @since 2021/12/10-14:04
      * @description 每次点击给相关词searchkey热度 +1
      **/
     @Override
    public Boolean incrementScore(String searchkey) {
        String key = searchkey;
        Long now = System.currentTimeMillis();
        ZSetOperations zSetOperations = stringRedisTemplate.opsForZSet();
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        //修改变量中的元素的分值。 分数+几
        zSetOperations.incrementScore("title", key, 1);
        valueOperations.getAndSet(key, String.valueOf(now));
        return true;
    }

    @Override
    public List<String> selectSearchResultList(Long userId) {
        String userIdKey = redisUtils.getSearchHistoryKey(userId);
        List<String> searchList = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().reverseRangeWithScores(userIdKey, 0, 9);
        Iterator<ZSetOperations.TypedTuple<String>> iterator = typedTuples.iterator();
        BigDecimal bigDecimal = null;
        while (iterator.hasNext()){
            ZSetOperations.TypedTuple<String> next = iterator.next();
            System.out.println(next);
             bigDecimal = BigDecimal.valueOf(next.getScore());
             if (next.getValue()!=null){
                 searchList.add(next.getValue());
             }
        }

        return searchList;
    }
    @Override
    public Boolean addSearchKey(Long userId,String historyKey) {
        System.out.println("进来了");
        String userIdKey = redisUtils.getSearchHistoryKey(userId);
        // 把用户ID当key，搜索内容当value  时间为socre   存入Redis  存集合的形式 historyKey是list的名称
//        stringRedisTemplate.opsForZSet().add(userIdKey,historyKey,System.currentTimeMillis());
        //存入reids之后对用户所有的搜索词要做处理   用户最多是个历史搜索词 根据时间来
        insertSearchSort(userIdKey,historyKey);
        incrementScore(historyKey);

        return true;
    }
    public void insertSearchSort(String userIdKey,String historyKey){
         Integer number = 10;
        Double score = stringRedisTemplate.opsForZSet().score(userIdKey, historyKey);
//        System.out.println("score"+score);
        if (score!=null){
            ////删除旧的搜索词   更新搜索词的时间
           stringRedisTemplate.opsForZSet().remove(userIdKey,historyKey);
        }
        //把用户ID当key，搜索内容当value  时间为socre   存入Redis  存集合的形式 historyKey是list的名称
        stringRedisTemplate.opsForZSet().add(userIdKey,historyKey,System.currentTimeMillis());
        //获取list集合中元素的个数。
        Long aLong = stringRedisTemplate.opsForZSet().zCard(userIdKey);
        if (aLong>number){
            //只留下number条数据 其他数据都移除掉
            stringRedisTemplate.opsForZSet().removeRange(userIdKey,0,aLong-number-1);
        }
    }

}
