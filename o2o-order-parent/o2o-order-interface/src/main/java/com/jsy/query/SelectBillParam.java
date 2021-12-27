package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className（类名称）: SelectBillParam
 * @description（类描述）: this is the SelectBillParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/24
 * @version（版本）: v1.0
 */
@Data
public class SelectBillParam {
    @ApiModelProperty("第几页")
    private Integer page;
    @ApiModelProperty("多少条数据")
    private Integer size;
    @ApiModelProperty("店铺id")
    private Long shopId;
}
