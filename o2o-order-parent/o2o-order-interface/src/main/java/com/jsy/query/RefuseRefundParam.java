package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className（类名称）: RefuseRefundParam
 * @description（类描述）: this is the RefuseRefundParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/1
 * @version（版本）: v1.0
 */
@Data
public class RefuseRefundParam {
    @ApiModelProperty(value = "退款申请id")
    private   Long refundId;
    @ApiModelProperty(value = "拒绝原因")
    private String refusalCause;
}
