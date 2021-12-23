package com.jsy.dto;


import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
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
    private Double grade=5.00;

    /**
     * 商品名称/服务标题
     */
    private String title;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 折扣价格
     */
    private BigDecimal discountPrice;
    /**
     * 折扣状态
     */
    private Integer discountState;
    /**
     * 距离
     */
    private String distance;

    /**
     * 距离备注
     */
    private Double distanceBak;

    /**
     * 是否超过配送范围 0 未超出 1 超出
     */
    private Integer state=0;

}
