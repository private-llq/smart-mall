package com.jsy.query;

import lombok.Data;

/**
 * @className（类名称）: OrderSizeParam
 * @description（类描述）: this is the OrderSizeParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/13
 * @version（版本）: v1.0
 */
@Data
public class OrderSizeParam {
    private Integer number;//近几日订单数量
    private Long shopId;//店铺id
}
