package com.jsy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @className（类名称）: SelectAllOrderByBackstageDto
 * @description（类描述）: this is the SelectAllOrderByBackstageDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/28
 * @version（版本）: v1.0
 */
@Data
public class SelectAllOrderByBackstageDto {
    @ApiModelProperty(value = "订单id")
    private Long id;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;


    @ApiModelProperty(value = "c端用户id")
    private Long userId;

    @ApiModelProperty(value = "b端商家id")
    private Long shopId;

    @ApiModelProperty(value = "订单编号")
    private String orderNum;

    @ApiModelProperty(value = "支付时间")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "消费方式（0用户到店，1商家上门）")
    private Integer consumptionWay;

    @ApiModelProperty(value = "订单的最终价格")
    private BigDecimal orderAllPrice;

    @ApiModelProperty(value = "联系人")
    private String linkman;

    @ApiModelProperty(value = "当前状态")
    private String  currentStatus;

    @ApiModelProperty(value = "商品详情")
    private List<SelectAllOrderBelongDto> things;
}
