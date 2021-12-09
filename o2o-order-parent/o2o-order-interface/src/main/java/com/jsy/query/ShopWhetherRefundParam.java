package com.jsy.query;

import lombok.Data;

/**
 * @className（类名称）: ShopWhetherRefundParam
 * @description（类描述）: this is the ShopWhetherRefundParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/8
 * @version（版本）: v1.0
 */
@Data
public class ShopWhetherRefundParam {
    private String refusalCause;//拒绝原因
    private Long orderId;//orderid
    private Integer accepts;//受理角色（0商家，1平台）

}
