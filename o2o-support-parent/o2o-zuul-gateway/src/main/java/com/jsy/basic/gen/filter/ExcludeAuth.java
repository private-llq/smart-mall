package com.jsy.basic.gen.filter;
import java.util.ArrayList;
import java.util.List;
/**
 * 不需要做token认证的URl
 */
public enum ExcludeAuth {
    INSTANCE;

    private List<String> execludeUrls = new ArrayList<>();

        ExcludeAuth(){
            /*execludeUrls.add("/services/user/user/token");//用户登陆*/
        }
        public boolean shouldFilter(String url) {
            if(url.matches(".*swagger.*")){
                return false;
            }
            if(url.matches(".*api-docs")){
                return false;
            }
            if(url.matches(".*/pub/.*")){
                return false;
            }
            if(url.matches(".*/order/.*")){
                return true;
            }
            if(execludeUrls.contains(url)) {
                return false;
            }
            return true;
    }
}
