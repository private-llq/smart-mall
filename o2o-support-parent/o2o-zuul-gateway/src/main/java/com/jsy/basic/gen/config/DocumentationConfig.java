package com.jsy.basic.gen.config;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import java.util.ArrayList;
import java.util.List;
@Component
@Primary
public class DocumentationConfig implements SwaggerResourcesProvider {

    @Override
    public List<SwaggerResource> get() {
        List resources = new ArrayList<>();
        resources.add(swaggerResource("商铺管理中心", "/services/shop/v2/api-docs", "2.0"));
        resources.add(swaggerResource("商品管理中心", "/services/goods/v2/api-docs", "2.0"));
        resources.add(swaggerResource("订单管理中心", "/services/order/v2/api-docs", "2.0"));
        resources.add(swaggerResource("用户管理中心", "/services/user/v2/api-docs", "2.0"));
        resources.add(swaggerResource("文件管理中心", "/services/file/v2/api-docs", "2.0"));
        resources.add(swaggerResource("客服管理中心", "/services/customer/v2/api-docs", "2.0"));
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location, String version){
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion(version);
        return swaggerResource;
    }
}