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
@ApiModel(value="店铺设置返回对象", description="店铺设置返回对象")
public class NewShopSetDto implements Serializable {

    private Long id;

    @ApiModelProperty(value = "店铺拥有者uuid")
    private Long ownerUuid;

    @ApiModelProperty(value = "店铺logo(file_url)")
    private String shopLogo;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "店铺联系电话")
    private String mobile;

    @ApiModelProperty(value = "营业执照地址）")
    private String businessAddress;
//    @ApiModelProperty(value = "详细地址（定位）")
//    private String addressDetail;

    @ApiModelProperty(value = "证件照片（展示给C端看的证件）")
    private String papers;

    @ApiModelProperty(value = "店铺环境照片")
    private String shopImages;

    @ApiModelProperty(value = "营业时间1")
    private String businessHours1;

    @ApiModelProperty(value = "营业时间2")
    private String businessHours2;

    @ApiModelProperty(value = "店铺公告")
    private String notice;

    //店铺评分
    private Double  score;
    //评价数量
    private Integer size;


}
