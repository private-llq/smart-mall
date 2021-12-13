package com.jsy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
public class NewShopInfo implements Serializable {
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "店铺拥有者uuid")
    private Long ownerUuid;

    @ApiModelProperty(value = "店铺所在城市")
    private String city;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店铺联系电话")
    private String mobile;

    @ApiModelProperty(name = "shop_tree_id", value = "门店类型id")
    private String shopTreeId;

    @ApiModelProperty(value = "店铺logo(file_url)")
    private String shopLogo;

    @ApiModelProperty(value = "营业执照图片(file_uuid)")
    private String businessImg;

    @ApiModelProperty(value = "注册号（工商户注册号，统一信用代码）")
    private String businessNumber;

    @ApiModelProperty(value = "公司名称（与营业执照的名称一致）")
    private String companyName;

    @ApiModelProperty(value = "店铺法人/经营者姓名")
    private String shopUsername;

    @ApiModelProperty(value = "店铺法人/经营者电话")
    private String shopPhone;

    @ApiModelProperty(value = "联系人身份证正面fileurl")
    private String shopCidZimg;

    @ApiModelProperty(value = "联系人身份证反面fileurl")
    private String shopCidFimg;

    @ApiModelProperty(value = "身份证名字")
    private String idName;

    @ApiModelProperty(value = "身份证号")
    private String idNumber;

//    @ApiModelProperty(value = "银行卡图片")
//    private String bankImages;
//
//    @ApiModelProperty(value = "银行卡卡号")
//    private String bankNumber;
//
//    @ApiModelProperty(value = "开户支行")
//    private String depositBank;
//
//    @ApiModelProperty(value = "开户银行")
//    private String branchBank;
//
//    @ApiModelProperty(value = "支付宝账号")
//    private String aliPay;
//
//    @ApiModelProperty(value = "微信账号")
//    private String weChat;

//    @ApiModelProperty(value = "详细地址（定位）")
//    private String addressDetail;

    @ApiModelProperty(value = "营业执照地址）")
    private String businessAddress;

    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "维度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "经营类型（0-服务行业   1-非服务行业）")
    private Integer type;


    @ApiModelProperty(value = "审核状态 0未审核 1已审核 2审核未通过  3资质未认证")
    private Integer state;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty(name = "shopTreeIdName", value = "门店类型名称")
    private String shopTreeIdName;

    @ApiModelProperty(value = "是否被屏蔽   1已屏蔽  0未屏蔽")
    private Integer shielding;

    @ApiModelProperty(value = "是否支持上门服务：0 不支持 1 支持")
    private Integer isVisitingService;
    @ApiModelProperty(value = "是否是虚拟店铺  0不是 1是")
    private Integer isVirtualShop;
}
