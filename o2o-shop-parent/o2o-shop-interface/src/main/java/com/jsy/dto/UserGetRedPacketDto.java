package com.jsy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "{用户}领取红包对象")
@Data
public class UserGetRedPacketDto {

    /**
     * 领取是否成功
     */
    @ApiModelProperty(value = "领取是否成功" )
    private boolean b;
    /**
     * 领取红包的金额
     */
    @ApiModelProperty(value = "红包金额" )
    private BigDecimal redPrice;

}
