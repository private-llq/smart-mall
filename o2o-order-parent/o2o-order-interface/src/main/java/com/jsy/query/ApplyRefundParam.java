package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className（类名称）: ApplyRefundParam
 * @description（类描述）: this is the ApplyRefundParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/26
 * @version（版本）: v1.0
 */
@Data
public class ApplyRefundParam {
    @ApiModelProperty(value = "退款原因")
    private String refund;

    @ApiModelProperty(value = "退款的订单id")
    private Long orderId;

    @ApiModelProperty(value = "图片")
    private String refundPicture;

    @ApiModelProperty(value = "受理角色（0商家，1平台）")
    private Integer accepts;
}
