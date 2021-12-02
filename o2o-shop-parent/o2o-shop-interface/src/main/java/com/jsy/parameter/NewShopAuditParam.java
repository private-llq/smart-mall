package com.jsy.parameter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
@Data
public class NewShopAuditParam implements Serializable {
    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "驳回理由")
    private String rejectedReason;

    @ApiModelProperty(value = "屏蔽理由")
    private String shieldingReason;

    @ApiModelProperty(value = "审核状态 0未审核 1已审核 2审核未通过 3资质未认证")
    private Integer state;

    @ApiModelProperty(value = "屏蔽状态 0未屏蔽  1已屏蔽")
    private Integer shielding;
}
