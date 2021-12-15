package com.jsy.dto;

import lombok.Data;

import java.util.List;

/**
 * @className（类名称）: OrderSizeDto
 * @description（类描述）: this is the OrderSizeDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/13
 * @version（版本）: v1.0
 */
@Data
public class OrderSizeDto {
    private  Integer amount;//总数
    private  List<OrderSizeDayDto>   list;//几个集合
}
