package com.jsy.parameter;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;


@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("店铺申请参数")
public class ShopInfoParam implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "city", value = "所在城市")
    private String city;
    @ApiModelProperty(name = "addressDetail", value = "店铺详细地址）")
    private String addressDetail;
    @ApiModelProperty(name = "shopUsername", value = "店铺法人/经营者姓名")
    private String shopUsername;
    @ApiModelProperty(name = "shopPhone", value = "店铺法人/经营者电话")
    private String shopPhone;
    @ApiModelProperty(name = "shopCidZimg", value = "身份证正面(file_url)")
    private String shopCidZimg;
    @ApiModelProperty(name = "shopCidFimg", value = "身份证反面(file_url)")
    private String shopCidFimg;
    @ApiModelProperty(name = "shopName", value = "店铺名称")
    private String shopName;
    @ApiModelProperty(name = "shopDescribe", value = "店铺描述")
    private String shopDescribe;
    @ApiModelProperty(name = "businessImg", value = "营业执照(file_url)")
    private String businessImg;
    @ApiModelProperty(name = "businessNumber", value = "注册号（工商户注册号，统一信用代码）")
    private String businessNumber;

    /***************************************** 将以前的多长  ********************************************************/
    @ApiModelProperty(name = "shopLogo", value = "店铺logo图片(file_url)")
    private String shopLogo;
    @ApiModelProperty(name = "shopFront", value = "店铺门脸图片(file_url)")
    private String shopFront;
    @ApiModelProperty(name = "shopImages1", value = "店铺环境图片1(file_url)")
    private String shopImages1;
    @ApiModelProperty(name = "shopImages2", value = "店铺环境图片2(file_url)")
    private String shopImages2;
    @ApiModelProperty(name = "shopImages3", value = "店铺环境图片3(file_url)")
    private String shopImages3;

    @ApiModelProperty(name = "longitude", value = "店铺地址经度")
    private BigDecimal longitude;
    @ApiModelProperty(name = "latitude", value = "店铺地址纬度")
    private BigDecimal latitude;


    /**********************************  新周边商家字段 ***************************************************************************/
    @ApiModelProperty(name = "shopSpecial", value = "店铺电话 座机")
    private String shopSpecial;
    @ApiModelProperty(name = "businessAddress", value = "营业执照地址")
    private String businessAddress;
    @ApiModelProperty(name = "industry_category_id", value = "门店类型id")
    private String industry_category_id;
    @ApiModelProperty(name = "companyName",value = "公司名称  营业执照的名称")
    private String companyName;
    @ApiModelProperty(name = "idName", value = "证件姓名")
    private String idName;
    @ApiModelProperty(name = "idNumber", value = "身份证号码")
    private String idNumber;

    @ApiModelProperty(name = "bankImages", value = "银行资料/开户许可证(file_url)")
    private String bankImages;
    @ApiModelProperty(name = "bankNumber", value = "银行卡号")
    private String bankNumber;
    @ApiModelProperty(name = "depositBank", value = "开户银行")
    private String depositBank;
    @ApiModelProperty(name = "branchBank", value = "开户支行")
    private String branchBank ;

    @ApiModelProperty(name = "aliPay", value = "支付宝账号")
    private String aliPay;
    @ApiModelProperty(name = "weChat", value = "微信账号")
    private String weChat ;





}
