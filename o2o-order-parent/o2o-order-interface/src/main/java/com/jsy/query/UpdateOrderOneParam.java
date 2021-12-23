package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @className（类名称）: UpdateOrderOneParam
 * @description（类描述）: this is the UpdateOrderOneParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/20
 * @version（版本）: v1.0
 */
@Data
public class UpdateOrderOneParam implements Serializable {
    @ApiModelProperty(value = "订单id")
    private  Long orderId;
    @ApiModelProperty(value = "0商品1服务2套餐")
    private Integer typeByGoods;
    @ApiModelProperty(value = "货物id")
    private Long commodityId;
    @ApiModelProperty(value = "数量")
    private Integer number;
}
