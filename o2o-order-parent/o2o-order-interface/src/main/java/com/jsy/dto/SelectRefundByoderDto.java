package com.jsy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className（类名称）: SelectRefundByoderDto
 * @description（类描述）: this is the SelectRefundByoderDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/8
 * @version（版本）: v1.0
 */
@Data
public class SelectRefundByoderDto {

    @ApiModelProperty(value = "退款原因")
    private String refund;

    @ApiModelProperty(value = "退款的订单id")
    private Long orderId;

    @ApiModelProperty(value = "是否同意退款（0申请中，1申请成功，2拒绝）")
    private Integer refundStatus;

    @ApiModelProperty(value = "拒绝原因")
    private String refusalCause;

    @ApiModelProperty(value = "图片")
    private String refundPicture;

    @ApiModelProperty(value = "受理角色（0商家，1平台）")
    private Integer accepts;
}
