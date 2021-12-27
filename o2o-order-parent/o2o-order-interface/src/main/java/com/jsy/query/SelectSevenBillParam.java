package com.jsy.query;

import lombok.Data;

/**
 * @className（类名称）: SelectSevenBillParam
 * @description（类描述）: this is the SelectSevenBillParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class SelectSevenBillParam {
    private Integer number;//近几日
    private Long shopId;//店铺id
}
