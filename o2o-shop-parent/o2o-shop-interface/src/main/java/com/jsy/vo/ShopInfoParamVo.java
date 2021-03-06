package com.jsy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("创建门店接收参数对象")
public class ShopInfoParamVo implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "所在城市不能为空")
    @ApiModelProperty(name = "city", value = "所在城市")
    private String city;

    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "店铺名称不能为空")
    @ApiModelProperty(name = "shopName", value = "店铺名称")
    private String shopName;

    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "联系人法人电话")
    @ApiModelProperty(name = "shopPhone", value = "联系人法人电话")
    private String shopPhone;

    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "地址不能为空")
    @ApiModelProperty(name = "businessAddress", value = "请填写营业执照上面的地址")
    private String businessAddress;
    /***************************************** 将以前的多张图片直接用List接收  ********************************************************/
    @NotEmpty(groups = {shopInfoParamVoValidatedGroup.class},message = "图片不能为空")
    @ApiModelProperty(name = "shopLogo", value = "店铺logo图片(file_url)")
    private List<String> shopLogo;
    /**********************************  新周边商家字段 ***************************************************************************/
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "门店类型不能为空")
    @ApiModelProperty(name = "industry_category_id", value = "门店类型id")
    private String industry_category_id;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "店铺详细地址不能为空")
    @ApiModelProperty(name = "addressDetail", value = "店铺详细地址（获取定位）")
    private String addressDetail;
    @NotEmpty(groups = {shopInfoParamVoValidatedGroup.class},message = "营业执照(不能为空")
    @ApiModelProperty(name = "businessImg", value = "营业执照(file_url)")
    private String businessImg;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "统一信用代码不能为空")
    @ApiModelProperty(name = "businessNumber", value = "注册号（工商户注册号，统一信用代码）")
    private String businessNumber;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "公司名称能为空")
    @ApiModelProperty(name = "companyName",value = "公司名称  营业执照的名称")
    private String companyName;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "店铺法人不能为空")
    @ApiModelProperty(name = "shopUsername", value = "店铺法人/经营者姓名")
    private String shopUsername;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "身份证正面不能为空")
    @ApiModelProperty(name = "shopCidZimg", value = "身份证正面(file_url)")
    private String shopCidZimg;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "身份证反面不能为空")
    @ApiModelProperty(name = "shopCidFimg", value = "身份证反面(file_url)")
    private String shopCidFimg;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "证件姓名不能为空")
    @ApiModelProperty(name = "idName", value = "证件姓名")
    private String idName;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "身份证号码不能为空")
    @ApiModelProperty(name = "idNumber", value = "身份证号码")
    private String idNumber;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "个人电话不能为空")
    @ApiModelProperty(name = "mobile", value = "座机电话")
    private String mobile;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "银行资料/开户许可证不能为空")
    @ApiModelProperty(name = "bankImages", value = "银行资料/开户许可证(file_url)")
    private String bankImages;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "银行卡号不能为空")
    @ApiModelProperty(name = "bankNumber", value = "银行卡号")
    private String bankNumber;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "开户银行不能为空")
    @ApiModelProperty(name = "depositBank", value = "开户银行")
    private String depositBank;
    @NotBlank(groups = {shopInfoParamVoValidatedGroup.class},message = "开户支行不能为空")
    @ApiModelProperty(name = "branchBank", value = "开户支行")
    private String branchBank ;

    @ApiModelProperty(name = "aliPay", value = "支付宝账号")
    private String aliPay;
    @ApiModelProperty(name = "weChat", value = "微信账号")
    private String weChat ;

    @ApiModelProperty(name = "longitude", value = "店铺地址经度")
    private BigDecimal longitude;
    @ApiModelProperty(name = "latitude", value = "店铺地址纬度")
    private BigDecimal latitude;

    public interface shopInfoParamVoValidatedGroup{}



}
