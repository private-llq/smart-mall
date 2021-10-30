package com.jsy.utils;

import com.alibaba.fastjson.JSONArray;
import com.gnete.openapi.internal.DefaultOpenApiRequestClient;
import com.jsy.bank.qo.OpenApiRequestQO;
import com.jsy.bank.vo.OpenApiResponseVO;
import com.jsy.basic.util.constant.UnionPayConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: Pipi
 * @Description: 银联支付工具类
 * @Date: 2021/4/11 11:31
 * @Version: 1.0
 **/
@Component
@Slf4j
public class UnionPayUtils {

    /**
     *@Author: Pipi
     *@Description: 交易业务的向银联接口发送请求并返回接收数据
     *@Param: jsonString: 发送的json字符串对象
     *@Param: apiName: 请求的api方法
     *@Return: com.jsy.community.vo.OpenApiResponseVO
     *@Date: 2021/4/11 13:34
     **/
    public OpenApiResponseVO transApi(String jsonString, String apiName) {
        log.info("银联接口请求参数++++++++++{}", jsonString);
        DefaultOpenApiRequestClient<OpenApiResponseVO, OpenApiRequestQO> client = buildClient();
        OpenApiRequestQO openApiRequestDemo = new OpenApiRequestQO(jsonString);
        openApiRequestDemo.setResponseClass(OpenApiResponseVO.class);
        openApiRequestDemo.setApiVersion("1.0.1");
        openApiRequestDemo.setApiInterfaceId(UnionPayConfig.TRANS_METHOD_HEADER);
        openApiRequestDemo.setApiMethodName(apiName);
        openApiRequestDemo.setNeedSign(true);
        OpenApiResponseVO response = client.requestApi(openApiRequestDemo);
        log.info("银联接口响应结果+++++{}", response.toString());
        return response;
    }

    public OpenApiResponseVO credentialApi(String jsonString, String apiName) {
        log.info("银联接口请求参数++++++++++{}", jsonString);
        DefaultOpenApiRequestClient<OpenApiResponseVO, OpenApiRequestQO> client = buildClient();
        OpenApiRequestQO openApiRequestDemo = new OpenApiRequestQO(jsonString);
        openApiRequestDemo.setResponseClass(OpenApiResponseVO.class);
        openApiRequestDemo.setApiVersion("1.0.1");
        openApiRequestDemo.setApiInterfaceId(UnionPayConfig.CREDENTIALS_METHOD_HEADER);
        openApiRequestDemo.setApiMethodName(apiName);
        openApiRequestDemo.setNeedSign(true);
        OpenApiResponseVO response = client.requestApi(openApiRequestDemo);
        log.info("银联接口响应结果+++++{}", response.toString());
        return response;
    }

    /**
     *@Author: Pipi
     *@Description: 交易业务的向银联接口发送请求并返回接收数据
     *@Param: jsonString: 发送的json字符串对象
     *@Param: apiName: 请求的api方法
     *@Return: com.jsy.community.vo.OpenApiResponseVO
     *@Date: 2021/4/14 14:14
     **/
    public OpenApiResponseVO queryApi(String jsonString, String apiName) {
        log.info("银联接口请求参数++++++++++{}", jsonString);
        DefaultOpenApiRequestClient<OpenApiResponseVO, OpenApiRequestQO> client = buildClient();
        OpenApiRequestQO openApiRequestDemo = new OpenApiRequestQO(jsonString);
        openApiRequestDemo.setResponseClass(OpenApiResponseVO.class);
        openApiRequestDemo.setApiVersion("1.0.1");
        openApiRequestDemo.setApiInterfaceId(UnionPayConfig.QUERY_METHOD_HEADER);
        openApiRequestDemo.setApiMethodName(apiName);
        openApiRequestDemo.setNeedSign(true);
        OpenApiResponseVO response = client.requestApi(openApiRequestDemo);
        log.info("银联接口响应结果+++++{}", response.toString());
        return response;
    }

    /**
     *@Author: Pipi
     *@Description: 红包的向银联接口发送请求并返回接收数据
     *@Param: jsonString: 发送的json字符串对象
     *@Param: apiName: 请求的api方法
     *@Return: com.jsy.community.vo.OpenApiResponseVO
     *@Date: 2021/4/11 13:34
     **/
    public OpenApiResponseVO redPacketApi(String jsonString, String apiName) {
        log.info("银联接口请求参数++++++++++{}", jsonString);
        DefaultOpenApiRequestClient<OpenApiResponseVO, OpenApiRequestQO> client = buildClient();
        OpenApiRequestQO openApiRequestDemo = new OpenApiRequestQO(jsonString);
        openApiRequestDemo.setResponseClass(OpenApiResponseVO.class);
        openApiRequestDemo.setApiVersion("1.0.1");
        openApiRequestDemo.setApiInterfaceId(UnionPayConfig.RED_PACKET_METHOD_HEADER);
        openApiRequestDemo.setApiMethodName(apiName);
        openApiRequestDemo.setNeedSign(true);
        OpenApiResponseVO response = client.requestApi(openApiRequestDemo);
        log.info("银联接口响应结果+++++{}", response.toString());
        return response;
    }


    /**
     *@Author: Pipi
     *@Description: 构建含msgBody的json字符串
     *@Param: o:
     *@Return: java.lang.String
     *@Date: 2021/4/10 14:02
     **/
    public String buildMsgBody(Object o) {
        Object jsonObject = JSONArray.toJSON(o);
        String jsonString = jsonObject.toString();
        StringBuilder stringBuilder = new StringBuilder("{\"msgBody\":");
        stringBuilder = stringBuilder.append(jsonString).append("}");
        return String.valueOf(stringBuilder);
    }

    /**
     *@Author: Pipi
     *@Description: 构建json字符串
     *@Param: o:
     *@Return: java.lang.String
     *@Date: 2021/5/8 9:47
     **/
    public String buildBizContent(Object o) {
        Object jsonObject = JSONArray.toJSON(o);
        String jsonString = jsonObject.toString();
        return jsonString;
    }


    /**
     *@Author: Pipi
     *@Description: 构建请求客户
     *@Param: :
     *@Return: com.gnete.openapi.internal.DefaultOpenApiRequestClient<com.jsy.community.common.OpenApiResponseDemo,com.jsy.community.common.OpenApiRequestDemo>
     *@Date: 2021/4/10 14:00
     **/
    private DefaultOpenApiRequestClient<OpenApiResponseVO, OpenApiRequestQO> buildClient() {
        String certificatePath = UnionPayUtils.class.getResource("/").getPath().replaceFirst("/", "");

        certificatePath = certificatePath+ "certificate/";
        //certificatePath = "/mnt/sdg/smart-mall/services/certificate";
        DefaultOpenApiRequestClient<OpenApiResponseVO, OpenApiRequestQO> client =
                DefaultOpenApiRequestClient.builder(UnionPayConfig.TEST_REQUEST_URL,
                        UnionPayConfig.APP_ID,
                        UnionPayConfig.SHA1_WITH_RSA).setHexPrivateKey(certificatePath + UnionPayConfig.PRIVATE_KEY)
                        .setHexPublicKey(certificatePath + UnionPayConfig.PUBLIC_KEY).setPrivateKeyPassword(UnionPayConfig.PRIVATE_KEY_PASS).build();
        return client;
    }

}