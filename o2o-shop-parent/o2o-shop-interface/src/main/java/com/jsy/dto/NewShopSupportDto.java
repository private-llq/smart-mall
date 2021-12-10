package com.jsy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NewShopSupportDto implements Serializable {

    @ApiModelProperty(value = "是否支持上门服务：0 不支持 1 支持")
    private Integer isVisitingService;
    @ApiModelProperty(value = "是否是虚拟店铺  0不是 1是")
    private Integer isVirtualShop;
    @ApiModelProperty(value = "店铺联系电话")
    private String mobile;
    @ApiModelProperty(value = "店铺法人/经营者电话")
    private String shopPhone;

    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;
    @ApiModelProperty(value = "店铺logo(file_url)")
    private String shopLogo;

}
