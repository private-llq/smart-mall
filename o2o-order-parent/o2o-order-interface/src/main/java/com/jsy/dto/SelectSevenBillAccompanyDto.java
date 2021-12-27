package com.jsy.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @className（类名称）: SelectSevenBillAccompanyDto
 * @description（类描述）: this is the SelectSevenBillAccompanyDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class SelectSevenBillAccompanyDto {
    private LocalDate time;//时间
    private BigDecimal oneDaymoney;//当前金额总和
}
