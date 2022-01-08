package com.jsy.parameter;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 创建店铺基本信息参数接收表
 * </p>
 *
 * @author Tian
 * @since 2021-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NewShopParam implements Serializable {

    private Long id;
    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "所在城市不能为空")
    @ApiModelProperty(name = "city", value = "所在城市")
    private String city;

    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "店铺名称不能为空")
    @ApiModelProperty(name = "shopName", value = "店铺名称")
    private String shopName;

    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "门店电话不能为空")
    @ApiModelProperty(name = "mobile", value = "门店电话")
    private String mobile;

    @NotEmpty(groups = {NewShopParam.newShopValidatedGroup.class},message = "店铺门头照不能为空")
    @ApiModelProperty(name = "shopLogo", value = "店铺logo图片(file_url)")
    private String shopLogo;

    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "地址不能为空")
    @ApiModelProperty(name = "businessAddress", value = "请填写营业执照上面的地址")
    private String businessAddress;

    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "门店类型不能为空")
    @ApiModelProperty(name = "shop_tree_id", value = "门店类型id")
    private String shopTreeId;

    @ApiModelProperty(value = "是否支持上门服务：0 不支持 1 支持")
    private Integer isVisitingService;
    @ApiModelProperty(value = "是否是虚拟店铺  0不是 1是")
    private Integer isVirtualShop;

    @NotNull(groups = {NewShopParam.newShopValidatedGroup.class},message = "用户定位地址不能为空")
    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @NotNull(groups = {NewShopParam.newShopValidatedGroup.class},message = "用户定位地址不能为空")
    @ApiModelProperty(value = "维度")
    private BigDecimal latitude;

    @ApiModelProperty(value = "是否是官方店铺  0不是 1是")
    private Integer isOfficialShop;





//    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "联系人法人电话")
//    @ApiModelProperty(name = "shopPhone", value = "联系人法人电话")
//    private String shopPhone;
//
//    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "店铺详细地址不能为空")
//    @ApiModelProperty(name = "addressDetail", value = "店铺详细地址（获取定位）")
//    private String addressDetail;
//
//    @NotEmpty(groups = {NewShopParam.newShopValidatedGroup.class},message = "营业执照(不能为空")
//    @ApiModelProperty(name = "businessImg", value = "营业执照(file_url)")
//    private String businessImg;
//
//    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "统一信用代码不能为空")
//    @ApiModelProperty(name = "businessNumber", value = "注册号（工商户注册号，统一信用代码）")
//    private String businessNumber;
//
//    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "公司名称能为空")
//    @ApiModelProperty(name = "companyName",value = "公司名称  营业执照的名称")
//    private String companyName;
//
//    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "店铺法人不能为空")
//    @ApiModelProperty(name = "shopUsername", value = "店铺法人/经营者姓名")
//    private String shopUsername;
//
//    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "身份证正面不能为空")
//    @ApiModelProperty(name = "shopCidZimg", value = "身份证正面(file_url)")
//    private String shopCidZimg;
//
//    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "身份证反面不能为空")
//    @ApiModelProperty(name = "shopCidFimg", value = "身份证反面(file_url)")
//    private String shopCidFimg;
//
//    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "证件姓名不能为空")
//    @ApiModelProperty(name = "idName", value = "证件姓名")
//    private String idName;
//
//    @NotBlank(groups = {NewShopParam.newShopValidatedGroup.class},message = "身份证号码不能为空")
//    @ApiModelProperty(name = "idNumber", value = "身份证号码")
//    private String idNumber;
//
//    @NotNull(groups = {NewShopParam.newShopValidatedGroup.class},message = "用户定位地址不能为空")
//    @ApiModelProperty(value = "经度")
//    private BigDecimal longitude;
//
//    @NotNull(groups = {NewShopParam.newShopValidatedGroup.class},message = "用户定位地址不能为空")
//    @ApiModelProperty(value = "维度")
//    private BigDecimal latitude;

    public interface newShopValidatedGroup{}
}
