package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;

/**
 * @className（类名称）: SelectWithdrawMonthParam
 * @description（类描述）: this is the SelectWithdrawMonthParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class SelectWithdrawMonthParam {

    @ApiModelProperty("年")
    private String year;
    @ApiModelProperty("月")
    private String month;
    @ApiModelProperty("店铺id")
    private Long shopId;
}
