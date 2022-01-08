package com.jsy.dto;



import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="C端店铺信息", description="C端展示店铺信息返回")
public class NewShopRecommendDto implements Serializable {
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(name = "shopTreeIdName", value = "门店类型名称")
    private String shopTreeIdName;


    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "商品名称/服务标题")
    private String title;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "评分")
    private Double grade;

    @ApiModelProperty(value = "距离多远")
    private BigDecimal distance;
    @ApiModelProperty
    private String shopLogo;
    @ApiModelProperty(value = "服务行业：1是服务行业  0 套餐行业")
    private Integer type;
    @ApiModelProperty(value = "营业执照地址）")
    private String businessAddress;
    @ApiModelProperty(value = "店铺联系电话")
    private String mobile;
    @ApiModelProperty(value = "店铺法人/经营者电话")
    private String shopPhone;

    @ApiModelProperty(value = "是否开启折扣：0未开启 1开启")
    private Integer discountState;
    @ApiModelProperty(value = "商品折扣价格")
    private BigDecimal discountPrice;
    @ApiModelProperty(value = "门店类型（与w_SHOP_TREE行业关联），用逗号分隔")
    private String shopTreeId;
    @ApiModelProperty(value = "是否是官方店铺  0不是 1是")
    private Integer isOfficialShop;


}
