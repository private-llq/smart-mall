package com.jsy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @className（类名称）: SelectUserOrderServiceDto
 * @description（类描述）: this is the SelectUserOrderServiceDto class
 * @author（创建人）: ${Administrator}
 * @createDate（创建时间）: 2021/11/16
 * @version（版本）: v1.0
 */
@Data
public class SelectUserOrderServiceDto {
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "服务 - 图片1-3张")
    private String images;

    @ApiModelProperty(value = "服务分类id(服务分类)")
    private Long goodsTypeId;

    @ApiModelProperty(value = "服务标题")
    private String title;

    @ApiModelProperty(value = "服务价格")
    private BigDecimal price;

    @ApiModelProperty(value = "折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "是否开启折扣：0未开启 1开启")
    private Integer discountState;

    @ApiModelProperty(value = "服务特点表集合")
    private List<ServiceCharacteristicsDto> ServiceCharacteristicsDtos;

    @ApiModelProperty(value = "服务的备注")
    private String textDescription;

    @ApiModelProperty(value = "服务电话")
    private String phone;

    @ApiModelProperty(value = "服务的使用规则")
    private String serviceRegulations;

    @ApiModelProperty(value = "服务有效期")
    private LocalDateTime validUntilTime;

    @ApiModelProperty(value = "服务数量")
    private Integer amount;
}