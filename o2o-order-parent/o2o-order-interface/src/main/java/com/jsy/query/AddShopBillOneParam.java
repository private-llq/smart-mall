package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @className（类名称）: AddShopBillOneParam
 * @description（类描述）: this is the AddShopBillOneParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/24
 * @version（版本）: v1.0
 */
@Data
public class AddShopBillOneParam {
    @ApiModelProperty(value = "订单id")
    @NotNull
    private Long orderId;

    @ApiModelProperty(value = "订单号")
    @NotNull
    private String orderNumber;

    @ApiModelProperty(value = "店铺id")
    @NotNull
    private Long shopId;

    @ApiModelProperty(value = "说明")
    private String explains;

    @ApiModelProperty(value = "日")
    private Integer day;

    @ApiModelProperty(value = "月份")
    @NotNull
    private Integer month;

    @ApiModelProperty(value = "年份")
    @NotNull
    private Integer year;

    @ApiModelProperty(value = "0收入1退款")
    @NotNull
    private Integer billType;

    @ApiModelProperty(value = "金额")
    @NotNull
    private BigDecimal money;
}
