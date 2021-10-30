package com.jsy.dto;

import com.jsy.domain.GoodsBasic;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ShopActiveDTO {

    //店铺uuid
    private String uuid;

    //店铺名称
    private String name;

    //活动名称
    private String activityName;

    //店铺logo图片
    private String shopLogo;

    //店铺地址
    private String shopAddress;

    //送达时间
    private int estimatedTime;

    //店铺经度
    private BigDecimal longitude;

    //店铺纬度
    private BigDecimal latitude;

    //店铺活动名称
    private List<String> activityNameList;

    //店铺商品
    private List<GoodsBasic> goodsBasicList;


}
