package com.jsy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class NewShopDistanceDto implements Serializable {
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
    @ApiModelProperty(value = "距离多远")
    private String distance;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "维度")
    private BigDecimal latitude;

    @ApiModelProperty(name = "businessAddress", value = "请填写营业执照上面的地址")
    private String businessAddress;
    @ApiModelProperty(value = "服务行业：1是服务行业  0 套餐行业")
    private Integer type;
    @ApiModelProperty(value = "店铺联系电话")
    private String mobile;
    @ApiModelProperty(value = "店铺法人/经营者电话")
    private String shopPhone;
    @ApiModelProperty(value = "聊天账号")
    private String imId;
    @ApiModelProperty(value = "是否是官方店铺  0不是 1是")
    private Integer isOfficialShop;
}
