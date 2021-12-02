package com.jsy.dto;

import com.jsy.parameter.NewShopParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

@Data
public class NewShopBasicDto implements Serializable {
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

    @ApiModelProperty(name = "shopTreeIdName", value = "门店类型名称")
    private String shopTreeIdName;

    @ApiModelProperty(value = "是否支持上门服务：0 不支持 1 支持")
    private Integer isVisitingService;
    @ApiModelProperty(value = "是否是虚拟店铺  0不是 1是")
    private Integer isVirtualShop;
}
