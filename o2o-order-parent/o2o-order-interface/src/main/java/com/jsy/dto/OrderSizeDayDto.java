package com.jsy.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @className（类名称）: OrderSizeDayDto
 * @description（类描述）: this is the OrderSizeDayDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/13
 * @version（版本）: v1.0
 */
@Data
public class OrderSizeDayDto {
    private LocalDate   time;//时间
    private Integer  number;//当前订单数量
}
