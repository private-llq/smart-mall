package com.jsy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @className（类名称）: SelectSevenBillDto
 * @description（类描述）: this is the SelectSevenBillDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class SelectSevenBillDto {

    @ApiModelProperty(value = "成交额")
    private BigDecimal  total;
    @ApiModelProperty(value = "近几天的数据")
    private List<SelectSevenBillAccompanyDto> list;

}
