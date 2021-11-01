package com.jsy.basic.gen.filter;

import com.alibaba.fastjson.JSONObject;
import com.jsy.basic.util.utils.JwtUtils;
import com.jsy.basic.util.vo.CommonResult;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;


@Component
public class AuthFilter extends ZuulFilter{
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }
    @Override
    public int filterOrder() {
        return 1;
    }
    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    /*让网关是否生效 true 生效 false不生效  为放行路径就不生效*/
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        String url = request.getServletPath();
        return ExcludeAuth.INSTANCE.shouldFilter(url);
    }

    /**
     * 拦截请求，查询是否带了token
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        String jwtBearerToken = ctx.getRequest().getHeader("token");
        String token = null;
        if(jwtBearerToken!=null){
            token = jwtBearerToken;
        }

        if (jwtBearerToken !=null && jwtBearerToken.startsWith("Bearer ")){
            token = jwtBearerToken.substring(7);
        }
        if (Objects.nonNull(token)&& StringUtils.isNotEmpty(stringRedisTemplate.opsForValue().get("Login:" + token))) {//可能在redis
            ctx.setSendZuulResponse(true);
            ctx.set("isAuthSuccess", true);
        }else if (Objects.nonNull(token) && JwtUtils.checkToken(token)){//可能在jwt
            ctx.setSendZuulResponse(true);
            ctx.set("isAuthSuccess", true);
        } else {
            ctx.setSendZuulResponse(false);
            ctx.set("isAuthSuccess", false);
            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
            ctx.getResponse().setContentType("application/json;charset=UTF-8");
            ctx.setResponseBody(JSONObject.toJSONString(CommonResult.error(-2,"未经授权的调用")));//对象转成JSON返回
        }
        return null;
    }
}
