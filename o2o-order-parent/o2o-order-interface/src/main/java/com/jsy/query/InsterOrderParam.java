package com.jsy.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @className（类名称）: InsterOrderParam
 * @description（类描述）: this is the InsterOrderParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/9
 * @version（版本）: v1.0
 */
@Data
public class InsterOrderParam {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "b端商家id")
    private Long shopId;
//    @ApiModelProperty(value = "订单类型（0-服务类(只有服务)，1-普通类（套餐，单品集合））")
//    private Integer orderType;
    @ApiModelProperty(value = "消费方式（0用户到店，1商家上门）")
    private Integer consumptionWay;
    @ApiModelProperty(value = "订单的最终价格")
    private BigDecimal orderAllPrice;
    @ApiModelProperty(value = "用户配送地址id（针对商家上门）")
    private Long shippingAddress;
    @ApiModelProperty(value = "联系人")
    private String linkman;
    @ApiModelProperty(value = "性别    0  男  |  1 女")
    private Integer sex;
    @ApiModelProperty(value = "用户电话")
    private String telepone;
    @ApiModelProperty(value = "地区")
    private String district;
    @ApiModelProperty(value = "详细地址")
    private String detailedAddress;
    @ApiModelProperty(value = "预计最早时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime startTime;
    @ApiModelProperty(value = "预计最晚时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime entTime;

}
