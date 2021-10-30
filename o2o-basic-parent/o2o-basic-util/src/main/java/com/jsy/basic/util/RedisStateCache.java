package com.jsy.basic.util;

import me.zhyd.oauth.cache.AuthStateCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * Redis作为JustAuth的State的缓存
 *
 * @author ling
 * @date 2020-11-16 17:24
 */
@Configuration
@RefreshScope
@ConditionalOnClass(value = AuthStateCache.class)
public class RedisStateCache implements AuthStateCache {

	@Resource
	private RedisTemplate<String, String> redisTemplate;
	
	/**
	 * 缓存超时时间，默认3分钟
	 */
	private Integer timeout = 180;

	
	/**
	 * 存入缓存
	 *
	 * @param key   缓存key
	 * @param value 缓存内容
	 */
	@Override
	public void cache(String key, String value) {
		this.cache(key, value, timeout);
	}
	
	/**
	 * 存入缓存
	 *
	 * @param key     缓存key
	 * @param value   缓存内容
	 * @param timeout 指定缓存过期时间（毫秒）
	 */
	@Override
	public void cache(String key, String value, long timeout) {
		redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
	}
	
	/**
	 * 获取缓存内容
	 *
	 * @param key 缓存key
	 * @return 缓存内容
	 */
	@Override
	public String get(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	/**
	 * 是否存在 key，如果对应key的value值已过期，也返回false
	 *
	 * @param key 缓存key
	 * @return true：存在key，并且value没过期；false：key不存在或者已过期
	 */
	@Override
	public boolean containsKey(String key) {
		Long expire = redisTemplate.getExpire(key, TimeUnit.MILLISECONDS);
		if (expire == null) {
			expire = 0L;
		}
		return expire > 0;
	}
}