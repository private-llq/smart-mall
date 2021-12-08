package com.jsy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
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

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @JSONField(serializeUsing = ToStringSerializer.class)
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
    @ApiModelProperty(value = "是否支持上门服务：0 不支持 1 支持")
    private Integer isVisitingService;
    @ApiModelProperty(value = "是否是虚拟店铺  0不是 1是")
    private Integer isVirtualShop;
    @ApiModelProperty(value = "服务行业：1是服务行业  0 套餐行业")
    private Integer type;


}
