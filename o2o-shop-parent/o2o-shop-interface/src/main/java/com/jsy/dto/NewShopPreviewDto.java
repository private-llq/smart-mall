package com.jsy.dto;

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
@ApiModel(value="店铺预览对象", description="店铺预览对象返回参数对象")
public class NewShopPreviewDto implements Serializable {

    private Long id;

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

    @ApiModelProperty(name = "shopTreeIdName", value = "门店类型名称")
    private String shopTreeIdName;

    @ApiModelProperty(value = "店铺logo(file_url)")
    private String shopLogo;

//    @ApiModelProperty(value = "详细地址（定位）")
//    private String addressDetail;

    @ApiModelProperty(value = "审核状态 0未审核 1已审核 2审核未通过 3资质未认证")
    private Integer state;
    @ApiModelProperty(value = "营业执照地址）")
    private String businessAddress;



}
