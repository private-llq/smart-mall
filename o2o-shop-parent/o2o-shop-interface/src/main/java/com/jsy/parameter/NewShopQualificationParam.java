package com.jsy.parameter;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewShopQualificationParam implements Serializable {
    @NotNull(groups = {NewShopParam.newShopValidatedGroup.class},message = "联系人法人电话")
    @ApiModelProperty(name = "id", value = "店铺id")
    private Long id;


    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "联系人法人电话")
    @ApiModelProperty(name = "shopPhone", value = "联系人法人电话")
    private String shopPhone;

//
//    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "店铺详细地址不能为空")
//    @ApiModelProperty(name = "addressDetail", value = "店铺详细地址（获取定位）")
//    private String addressDetail;

    @NotEmpty(groups = {NewShopParam.newShopValidatedGroup.class},message = "营业执照(不能为空")
    @ApiModelProperty(name = "businessImg", value = "营业执照(file_url)")
    private String businessImg;

     @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "统一信用代码不能为空")
    @ApiModelProperty(name = "businessNumber", value = "注册号（工商户注册号，统一信用代码）")
    private String businessNumber;

     @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "公司名称能为空")
    @ApiModelProperty(name = "companyName",value = "公司名称  营业执照的名称")
    private String companyName;

     @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "店铺法人不能为空")
    @ApiModelProperty(name = "shopUsername", value = "店铺法人/经营者姓名")
    private String shopUsername;

    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "身份证正面不能为空")
    @ApiModelProperty(name = "shopCidZimg", value = "身份证正面(file_url)")
    private String shopCidZimg;

     @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "身份证反面不能为空")
    @ApiModelProperty(name = "shopCidFimg", value = "身份证反面(file_url)")
    private String shopCidFimg;

 @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "证件姓名不能为空")
    @ApiModelProperty(name = "idName", value = "证件姓名")
    private String idName;

   @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "身份证号码不能为空")
    @ApiModelProperty(name = "idNumber", value = "身份证号码")
    private String idNumber;

    @NotNull(groups = {NewShopParam.newShopValidatedGroup.class},message = "用户定位地址不能为空")
    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @NotNull(groups = {NewShopParam.newShopValidatedGroup.class},message = "用户定位地址不能为空")
    @ApiModelProperty(value = "维度")
    private BigDecimal latitude;

}
