package com.jsy.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 新——店铺表
 * </p>
 *
 * @author Tian
 * @since 2021-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="NewShop对象", description="店铺返回参数对象")
public class NewShopDto implements Serializable {

    private Long id;

    @ApiModelProperty(value = "店铺uuid")
    private String uuid;

    @ApiModelProperty(value = "店铺拥有者uuid")
    private String ownerUuid;

    @ApiModelProperty(value = "店铺所在城市")
    private String city;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店铺联系电话")
    private String mobile;

    @ApiModelProperty(value = "门店类型（与w_industry_category行业关联）")
    private String industryCategoryId;

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

    @ApiModelProperty(value = "银行卡图片")
    private String bankImages;

    @ApiModelProperty(value = "银行卡卡号")
    private String bankNumber;

    @ApiModelProperty(value = "开户支行")
    private String depositBank;

    @ApiModelProperty(value = "开户银行")
    private String branchBank;

    @ApiModelProperty(value = "支付宝账号")
    private String aliPay;

    @ApiModelProperty(value = "微信账号")
    private String weChat;

    @ApiModelProperty(value = "详细地址（定位）")
    private String addressDetail;

    @ApiModelProperty(value = "营业执照地址）")
    private String businessAddress;

    @ApiModelProperty(value = "经纬度（经度，维度））")
    private String lonLat;




}
