package com.jsy.dto;

import com.jsy.domain.Activity;
import com.jsy.domain.ShopRedpacket;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
  * @Author lxr
  * @Description 查询满足条件的商品或者店铺，数组组装代码执行
  * @Date 2020/12/3 15:17
  * @Param
  **/
@Data
@ApiModel("首页关键字查询数据库dto")
public class ShopQueryDTO<T> implements Serializable {

    @ApiModelProperty("商品或者店铺的uuid")
    private String uuid;


    @ApiModelProperty("店铺类型ID")
    private String shopTypeUuid;

    @ApiModelProperty("商品或者店铺的名称")
    private String name;

    @ApiModelProperty("商品或者店铺的图片id")
    private String images;

    @ApiModelProperty("商品的价格")
    private BigDecimal price;

    @ApiModelProperty("商品的折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty("店铺描述")
    private String shopDescribe;

    @ApiModelProperty("店铺星级")
    private Double star;

    @ApiModelProperty("店铺销量")
    private int monthlySale;

    @ApiModelProperty("店铺地址")
    private String address;

    @ApiModelProperty("店铺下的商品")
    private List<T> goodsList;

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    @ApiModelProperty("距离")
    private double distance;

    @ApiModelProperty("预计送达时间")
    private int estimatedTime;

    @ApiModelProperty("活动名称")
    private List<Activity> activityNameList;

    @ApiModelProperty(name = "shopRedpacketList",value = "店铺红包集合")
    private List<ShopRedpacket> shopRedpacketList;

    @ApiModelProperty(name = "shopServe",value = "店铺服务")
    private String shopServe;

    @ApiModelProperty("前端判断")
    private boolean show = true;


}
