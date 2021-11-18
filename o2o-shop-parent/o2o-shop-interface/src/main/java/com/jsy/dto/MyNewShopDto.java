package com.jsy.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MyNewShopDto implements Serializable {

    /**
     * 商店id
     */
    private Long id;

    /**
     * 店铺名称
     */
    private String shopName;

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