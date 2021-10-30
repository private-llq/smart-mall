package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("修改店铺单笔配送费")
public class UpdatePostageParam {
    private  String  shopUuid;
    private BigDecimal postage;
}
