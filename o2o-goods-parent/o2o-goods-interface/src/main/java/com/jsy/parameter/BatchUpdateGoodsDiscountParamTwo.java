package com.jsy.parameter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("批量修改商品的折扣参数对象two")
public class BatchUpdateGoodsDiscountParamTwo {
    @ApiModelProperty(name = "uuids",value = "商品uuids")
    private List<String> uuids;
    @ApiModelProperty(value = "商品折扣价格",name = "discountPrice")
    private BigDecimal discountPrice;
    @ApiModelProperty(value = "商品的折扣(0.10-0.99)",name = "discount")
    private Double discount;
    @ApiModelProperty(value = "1按照折扣比例0按照折扣价格",name = "discountStatus")
    private Integer discountStatus;
    @ApiModelProperty(value = "限购份数",name = "astrictNumber")
    private Integer astrictNumber;
    @ApiModelProperty(value = "折扣开始时间",name = "discountStartTime")
    private LocalDateTime discountStartTime;
    @ApiModelProperty(value = "折扣结束时间",name = "discountEndTime")
    private LocalDateTime discountEndTime;
    @ApiModelProperty(value = "折扣活动状态",name = "activityStatus")
    private Integer activityStatus;
}
