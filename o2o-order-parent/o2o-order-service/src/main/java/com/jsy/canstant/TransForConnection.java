package com.jsy.canstant;

import com.alibaba.fastjson.JSONObject;
import com.jsy.basic.util.exception.JSYException;
import com.jsy.basic.util.utils.HttpAddress;
import com.jsy.basic.util.utils.HttpUtil;

/**
 * @author 国民探花余公子
 * @ClassName TransForConnection
 * @Date 2021-05-20  11:20
 * @Description TODO
 * @Version 1.0
 **/
public class TransForConnection {


    public static String showWallet(String walletId,String isNeedPwd,String token){
        String str = HttpAddress.SHOW_MONEY;
        JSONObject params = new JSONObject();
        params.put("walletId",walletId);
        params.put("isNeedPwd",isNeedPwd);
        String s = null;
        try {
            s = HttpUtil.sendPost(str, params.toJSONString(),token);
            JSONObject result = JSONObject.parseObject(s);
            Object data = result.get("data");
            result = JSONObject.parseObject(data.toString());
            data = result.get("response");
            result = JSONObject.parseObject(data.toString());
            data = result.get("msgBody");
            result = JSONObject.parseObject(data.toString());
            data = result.get("bal");
            s = data.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new JSYException(-1,"调用社区查询钱包接口异常");
        }
        return s;
    }


    public static String userOpenAccount(String jumpType,String token){
       String str = HttpAddress.USER_OPEN_ACCOUNT;
       String callbackUrl = HttpAddress.BAI_DU;
        JSONObject params= new JSONObject();
        params.put("jumpType",jumpType);
        params.put("callbackUrl",callbackUrl);
        String s = null;
        try {
            s = HttpUtil.sendPost(str, params.toJSONString(), token);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return s;
    }



}