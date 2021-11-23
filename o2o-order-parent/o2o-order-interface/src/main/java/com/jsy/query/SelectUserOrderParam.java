package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @className（类名称）: SelectUserOrderParam
 * @description（类描述）: this is the SelectUserOrderParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/16
 * @version（版本）: v1.0
 */
@Data
public class SelectUserOrderParam {
    @ApiModelProperty("第几页")
    private Integer page;
    @ApiModelProperty("多少条数据")
    private Integer size;

    @ApiModelProperty(value = "订单状态（[1待上门、待配送、待发货]，2、完成）")
    private Integer orderStatus;

    @ApiModelProperty(value = "预约状态（0预约中，1预约成功）")
    private Integer appointmentStatus;

    @ApiModelProperty(value = "支付状态（0未支付，1支付成功,2退款中，3退款成功，4拒绝退款）")
    private Integer payStatus;

}
