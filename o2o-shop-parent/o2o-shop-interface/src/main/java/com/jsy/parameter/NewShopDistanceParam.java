package com.jsy.parameter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class NewShopDistanceParam implements Serializable {
    private Long id;
    @ApiModelProperty(value = "经度")
    private BigDecimal longitude;

    @ApiModelProperty(value = "维度")
    private BigDecimal latitude;
    /**
     * @author Tian
     * @since 2021/11/29-9:15
     * @description  店铺名称
     **/
    private String shopName;
}
