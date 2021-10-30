package com.jsy.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.math.BigDecimal;

@ApiModel("提现vo")
@Data
public class WithdrawVO {

    //账户名称
    private String name;

    //提现金额
    private BigDecimal money;

}
