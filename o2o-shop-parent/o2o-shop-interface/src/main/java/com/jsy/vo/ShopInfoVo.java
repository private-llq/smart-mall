package com.jsy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


@Data
@ApiModel("店铺信息vo")
public class ShopInfoVo {
    /**
     * 店铺uuid
     */
    @ApiModelProperty(name = "uuid",value = "店铺uuid")
    private String uuid;

    /**
     * 店铺类型id
     */
    @ApiModelProperty(name = "tid",value = "店铺类型id")
    private String shopTypeUuid;

    /**
     * 店铺名称
     */
    @ApiModelProperty(name = "name",value = "店铺名称")
    private String name;


    /**
     * 营业执照
     */
    @ApiModelProperty(name = "businessLicense",value = "营业执照图片地址")
    private String businessLicense;

    @ApiModelProperty(name = "shopLogo",value = "店铺logo ")
    private String shopLogo;

    /**
     * 店铺电话
     */
    @ApiModelProperty(name = "mobile",value = "店铺联系电话")
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
     * 店铺登录密码
     */
    private String password;

    /**
     * 联系人电话，用户登录
     */
    @ApiModelProperty(name = "shopPhone",value = "联系人电话，用于登录")
    private String shopPhone;

    /**
     * 联系人身份证
     */
    @ApiModelProperty(name = "shopCid",value = "联系人身份证号码")
    private String shopCid;

    /**
     * 店铺内部图片
     */
    @ApiModelProperty(name = "shopImages",value = "店铺内部图片")
    private String shopImages;

    /**
     * 店铺描述
     */
    @ApiModelProperty(name = "shopDescribe",value = "店铺描述")
    private String shopDescribe;

    //店铺营业时间
    @ApiModelProperty(name = "shopHours")
    private String shopHours;

    //店铺服务特色
    @ApiModelProperty(name = "shopServe")
    private String shopServe;

    /**
     * 店铺地址经度
     */
    @ApiModelProperty(name = "longitude",value = "店铺经度")
    private BigDecimal longitude;

    /**
     * 店铺地址纬度
     */
    @ApiModelProperty(name = "latitude",value = "店铺纬度")
    private BigDecimal latitude;

}
