package com.jsy.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("门店信息返回对象")
public class ShopMessageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "uuid", value = "店铺uuid")
    private String uuid;
    @ApiModelProperty(name = "ownerUuid", value = "店铺拥有者uuid")
    private String ownerUuid;
    @ApiModelProperty(name = "shopName", value = "店铺名称")
    private String shopName;
    @ApiModelProperty(name = "city", value = "所在城市")
    private String city;
    @ApiModelProperty(name = "addressDetail", value = "店铺详细地址）")
    private String addressDetail;
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
    @ApiModelProperty(name = "businessStatus", value = "营业状态1营业中，2休息中，3暂停营业")
    private Integer businessStatus;
    @ApiModelProperty(name = "deliveryArea", value = "配送范围(单位米)")
    private Long deliveryArea;
    @ApiModelProperty(name = "notice", value = "店铺公告")
    private String notice;
    @ApiModelProperty(name = "orderReceivingPhone", value = "接单电话最多三个用;隔开")
    private  String orderReceivingPhone;
    @ApiModelProperty(name = "BusinessTimeDtoS", value = "营业时间")
    private List<SelectShopBusinessTimeDto> BusinessTimeDtoS;
    @ApiModelProperty(name = "shop_sent", value = "是否是服务（1是服务类0商品类）")
    private String shopSent;


}
