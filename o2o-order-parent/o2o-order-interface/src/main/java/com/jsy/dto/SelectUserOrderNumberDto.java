package com.jsy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @className（类名称）: SelectUserOrderNumberDto
 * @description（类描述）: this is the SelectUserOrderNumberDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/22
 * @version（版本）: v1.0
 */
@Data
@AllArgsConstructor
public class SelectUserOrderNumberDto {
    //状态1代付款2待消费3已完成4售后
    private  Integer  status;
    //数量
    private Integer number;
}
