package com.jsy.basic.util.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jsy.basic.util.exception.JSYError;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.vo.UserDto;
import com.jsy.basic.util.vo.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
/**
 *获取当前登录用户
 */
@Slf4j
public class CurrentUserHolder {

    /**
     * 获取商家用户
     * @return
     */
    public static UserDto getCurrentUser() {
        //解析token
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        String substring = token.substring(7);
        if (StringUtils.isEmpty(substring)) {
            throw new JSYException(-1,"获取token信息失败，请稍后再试！");
        }
        //解析token
        UserDto userDto=null;
        try {
            userDto = JwtUtils.decodeToken(substring);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Objects.isNull(userDto)) {
            throw new JSYException(-1,"获取token信息失败，请重新登录！");
        }
        return userDto;
    }

    /**
     * 获取社区的用户
     * @return
     */
    public static UserEntity getUserEntity(){
        //解析token
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            throw new JSYException(-1,"获取token信息失败，请稍后再试！");
        }
        String url = HttpAddress.USER_DETAIL;
        String result = HttpUtil.sendGet(url,"", token);
        if (StringUtils.isEmpty(result)){
            log.error("请求地址：" + url);
            log.error("响应结果：" + result);
            throw new JSYException(JSYError.BAD_REQUEST.getCode(),"从社区获取用户信息失败");
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        Object data = jsonObject.get("data");
        //bean实例化
        ObjectMapper mapper=new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.registerModule(new JavaTimeModule());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        UserEntity userEntity = null;
        try {
            userEntity = mapper.convertValue(data, UserEntity.class);
        } catch (IllegalArgumentException e) {
            throw new JSYException(-1,"用户登录过期");//false
        }

        return userEntity;
    }

    public static String getToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        return token;
    }
}
