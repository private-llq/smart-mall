package com.jsy.vo;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @className（类名称）: OrderServiceVO
 * @description（类描述）: this is the OrderServiceVO class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class OrderServiceVO {
    private Long id;


    private LocalDateTime createTime;


    @ApiModelProperty(value = "服务 - 图片1-3张")
    private String images;


    @ApiModelProperty(value = "服务标题")
    private String title;

    @ApiModelProperty(value = "服务价格")
    private BigDecimal price;

    @ApiModelProperty(value = "折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "是否开启折扣：0未开启 1开启")
    private Integer discountState;


    @ApiModelProperty(value = "服务数量")
    private Integer amount;

    @ApiModelProperty(value = "服务id")
    private Long serviceId;
}
