package com.jsy.basic.util.config;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 自动设置创建时间、更新时间
 *
 * @author ling
 * @since 2020-11-11 15:47
 */
@Component
@Slf4j
@Configuration
//@ConditionalOnProperty(value = "jsy.service.enable", havingValue = "true")
public class MyMetaObjectHandler implements MetaObjectHandler {
	@Override
	public void insertFill(MetaObject metaObject) {
		System.out.println("自动填充时间");
		setFieldValByName("createTime", LocalDateTime.now(), metaObject);
		setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
		setFieldValByName("deleted", 0, metaObject);
	}
	@Override
	public void updateFill(MetaObject metaObject) {
		setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
	}
	// 逻辑删除组件
	@Bean
	public ISqlInjector sqlInjector() {
		return new DefaultSqlInjector();
	}

	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		//interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());//乐观锁组件
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.H2));//分页查询组件
		return interceptor;
	}
}