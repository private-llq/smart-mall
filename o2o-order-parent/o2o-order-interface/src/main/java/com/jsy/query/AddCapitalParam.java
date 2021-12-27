package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @className（类名称）: AddCapitalParam
 * @description（类描述）: this is the AddCapitalParam class
 * @author（创建人）: ${Administrator}
 * @createDate（创建时间）: 2021/12/24
 * @version（版本）: v1.0
 */
@Data
public class AddCapitalParam {
    @NotNull
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
    @NotNull
    @ApiModelProperty(value = "增加或者减少的金额")
    private BigDecimal money;
}
