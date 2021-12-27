package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @className（类名称）: AddShopWithdrawOneParam
 * @description（类描述）: this is the AddShopWithdrawOneParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/24
 * @version（版本）: v1.0
 */
@Data
public class AddShopWithdrawOneParam {
    @NotNull
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
    @NotNull
    @ApiModelProperty(value = "金额")
    private BigDecimal money;
    @NotNull
    @ApiModelProperty(value = "提现方式0微信1支付宝")
    private Integer withdrowWay;
}
