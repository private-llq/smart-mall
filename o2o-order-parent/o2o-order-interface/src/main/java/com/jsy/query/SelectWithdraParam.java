package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className（类名称）: SelectWithdraParam
 * @description（类描述）: this is the SelectWithdraParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class SelectWithdraParam {
    @ApiModelProperty("第几页")
    private Integer page;
    @ApiModelProperty("多少条数据")
    private Integer size;
    @ApiModelProperty("店铺id")
    private Long shopId;
}
