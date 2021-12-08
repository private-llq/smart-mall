package com.jsy.config;
import com.alibaba.fastjson.JSON;;
import com.jsy.basic.util.vo.CommonResult;
import com.jsy.utils.AliAppPayQO;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class HttpClientHelper {


    private HttpClientHelper() {

    }

    /**
     * 发起POST请求
     *@param token     token
     *@param url       url
     *@param object    参数对象哦
     *@param clazz     返回参数对象字节码
     */
    public static  <T> T sendPost(String url, Object object,String token, Class<T> clazz) {
        //将数据变成json格式
        String paramJson = JSON.toJSONString(object);
        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 创建post请求方法实例对象
        PostMethod postMethod = new PostMethod(url);
        // 设置post请求超时时间
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        postMethod.getParams().setContentCharset("utf-8");
        postMethod.addRequestHeader("Content-Type", "application/json");
        postMethod.addRequestHeader("token", token);//在请求头中添加token
        try {
            //json格式的参数解析

            RequestEntity entity = new StringRequestEntity(paramJson, "application/json", "UTF-8");
            postMethod.setRequestEntity(entity);
            httpClient.executeMethod(postMethod);
            String result = postMethod.getResponseBodyAsString();
            postMethod.releaseConnection();
            return JSON.parseObject(result, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 发起GET请求
     *@param  token  token
     *@param urlParam url请求，包含参数
      @param clazz    返回参数对象字节码
     */
    public static <T> T  sendGet(String urlParam,String token, Class<T> clazz) {

        // 创建httpClient实例对象
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(15000);
        // 创建GET请求方法实例对象
        GetMethod getMethod = new GetMethod(urlParam);
        // 设置post请求超时时间
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 60000);
        getMethod.addRequestHeader("Content-Type", "application/json");
        getMethod.addRequestHeader("token", token);
        try {
            httpClient.executeMethod(getMethod);
            String result = getMethod.getResponseBodyAsString();
            getMethod.releaseConnection();
            return JSON.parseObject(result, clazz);
        } catch (IOException e) {
             e.printStackTrace();
        }
        return null;
    }



    public static void main(String[] args) {

//        String token ="E_371fa2c6-417e-4408-a2e9-0d99995a18c6";
//
//        String url = "http://192.168.12.49:8090/shop/order/newComment/selectShopCommentScore?shopId=1457644731467661314";
//        CommonResult commonResult = sendGet(url, token, CommonResult.class);
//        Object data = commonResult.getData();
//        System.out.println(commonResult);
//        System.out.println(data);



//        NewReply newReply=new NewReply();
//        newReply.setReply("123");
//        newReply.setCommentId(123L);
//        newReply.setDeleted(0);
//        String s1 = JSON.toJSONString(newReply);//对象专json
//        System.out.println(s1);
//        NewReply newReply1 = JSON.parseObject(s1, NewReply.class);//json专对象
//        System.out.println(newReply1);




//        CreateCommentParam commentParam=new CreateCommentParam();
//        commentParam.setEvaluateLevel(3);
//        commentParam.setImages("http图片");
//        commentParam.setOrderId(55555555L);
//        commentParam.setShopId(6666666L);
//        String urlParam = "http://192.168.12.49:8090/shop/order/newComment/createComment";
//        CommonResult commonResult1 = sendPost(urlParam, commentParam, token, CommonResult.class);
//        System.out.println(commonResult1);



    }

}