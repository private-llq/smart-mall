package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 退款申请表
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_new_refund")
@ApiModel(value="NewRefund对象", description="退款申请表")
public class NewRefund extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
