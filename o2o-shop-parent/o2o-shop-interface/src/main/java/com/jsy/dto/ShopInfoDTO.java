package com.jsy.dto;

import com.jsy.domain.Activity;
import com.jsy.domain.ShopRedpacket;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ApiModel("用户查看店铺返回DTO")
public class ShopInfoDTO {
    /**
     * 店铺uuid
     */
    @ApiModelProperty(name = "uuid",value = "店铺uuid")
    private String uuid;

    /**
     * 店铺类型id
     */
    @ApiModelProperty(name = "shopTypeUuid",value = "店铺类型uuid")
    private String shopTypeUuid;

    /**
     * 店铺名称
     */
    @ApiModelProperty(name = "id",value = "店铺名称")
    private String name;


    /**
     * 店铺电话
     */
    @ApiModelProperty(name = "mobile",value = "店铺电话")
    private String mobile;


    /**
     * 店铺地址
     */
    @ApiModelProperty(name = "addressDetail",value = "店铺地址")
    private String addressDetail;

    /**
     * 店铺联系人
     */
    @ApiModelProperty(name = "shopUsername",value = "店铺联系人")
    private String shopUsername;

    /**
     * 联系人电话
     */
    @ApiModelProperty(name = "shopPhone",value = "联系人电话")
    private String shopPhone;

    /**
     * 店铺logo
     */
    @ApiModelProperty(name = "shopLogo",value = "店铺logo")
    private String shopLogo;

    /**
     * 店铺图片
     */
    @ApiModelProperty(name = "shopImages",value = "店铺图片")
    private String shopImages;

    /**
     * 店铺描述
     */
    @ApiModelProperty(name = "shopDescribe",value = "店铺描述")
    private String shopDescribe;

    /**
     * 店铺地址经度
     */
    @ApiModelProperty(name = "longitude",value = "店铺地址经度")
    private BigDecimal longitude;

    //店铺营业时间
    @ApiModelProperty(name = "shopHours")
    private String shopHours;

    //店铺服务特色
    @ApiModelProperty(name = "shopServe")
    private String shopServe;

    //月售
    @ApiModelProperty(name = "monthlySale")
    private Integer monthlySale;

    //送达时间
    @ApiModelProperty(name = "estimatedTime")
    private Integer estimatedTime;

    /**
     * 店铺地址纬度
     */
    @ApiModelProperty(name = "latitude",value = "店铺地址纬度")
    private BigDecimal latitude;

    @ApiModelProperty(name = "activityList",value = "店铺活动集合")
    private List<Activity> activityList;

    @ApiModelProperty(name = "shopRedpacketList",value = "店铺红包集合")
    private List<ShopRedpacket> shopRedpacketList;




}
