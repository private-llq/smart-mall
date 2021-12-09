package com.jsy.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MyNewShopDto implements Serializable {

    /**
     * 商店id
     */
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 店铺图片
     */
    private String image;

    /**
     * 门店类型名称
     */
    private String shopTreeIdName;

    /**
     * 店铺评分
     */
    private Float grade=5.0f;

    /**
     * 商品名称/服务标题
     */
    private String title;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 距离
     */
    private String distance;

}
