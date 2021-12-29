package com.jsy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @className（类名称）: SelectAllOrderBelongDto
 * @description（类描述）: this is the SelectAllOrderBelongDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/28
 * @version（版本）: v1.0
 */
@Data
public class SelectAllOrderBelongDto {
    @ApiModelProperty(value = "名字")
    private  String  name;
    @ApiModelProperty(value = "单价")
    private BigDecimal price;
    @ApiModelProperty(value = "图片")
    private String  pictures;
    @ApiModelProperty(value = "数量")
    private Integer number;
}
