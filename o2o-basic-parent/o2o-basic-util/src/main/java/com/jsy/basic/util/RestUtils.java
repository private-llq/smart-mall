package com.jsy.basic.util;

import com.jsy.basic.util.vo.CommonResult;
import io.jsonwebtoken.Claims;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;



public class RestUtils {
    private RestTemplate restTemplate=new RestTemplate();

    /**
     * @param url url地址
     * @param uriVariables url地址参数，如果没有url参数的时候直接传空Map(new HashMap())
     * @return 返回基础数据类
     */
    public CommonResult getObject(String url, Map<String, ?> uriVariables){
        CommonResult result = restTemplate.getForObject(url, CommonResult.class, uriVariables);
        return result;
    }


    /**
     * @param url url地址
     * @param uriVariables url地址参数，如果没有url参数的时候直接传空Map(new HashMap())
     * @return 返回基础数据类，带响应状态
     */
    public ResponseEntity<CommonResult> getResponse(String url, Map<String, ?> uriVariables){
        return restTemplate.getForEntity(url, CommonResult.class, uriVariables);
    }

    /**
     *
     * @param url url地址
     * @param request 请求实体对象
     * @param uriVariables  url地址参数，如果没有url参数的时候直接传空Map(new HashMap())
     * @return 返回基础数据类
     */
    public CommonResult postObject(String url, @Nullable Object request,Map<String, ?> uriVariables){
      return restTemplate.postForObject(url,request,CommonResult.class,uriVariables);
    }

    /**
     *
     * @param url url地址
     * @param request 请求实体对象
     * @param uriVariables  url地址参数，如果没有url参数的时候直接传空Map(new HashMap())
     * @return 返回基础数据类，带响应状态
     */
    public ResponseEntity<CommonResult> postResponse(String url, @Nullable Object request, Map<String, ?> uriVariables){
        return restTemplate.postForEntity(url,request,CommonResult.class,uriVariables);

    }

    public static void main(String[] args) {
        Long id=1460800913757761538L;
        RestUtils restUtils = new RestUtils();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id",id);
        CommonResult result = restUtils.getObject(GetServiceName.GOODS + "goods/getGoodsService/" + "id={id}", map);
        System.out.println(result.getData());
    }

}
