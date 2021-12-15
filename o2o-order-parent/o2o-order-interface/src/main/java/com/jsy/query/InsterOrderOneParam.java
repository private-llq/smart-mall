package com.jsy.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsy.domain.Goods;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;


/**
 * @className（类名称）: InsterOrderOneParam
 * @description（类描述）: this is the InsterOrderOneParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/11
 * @version（版本）: v1.0
 */
@Data
public class InsterOrderOneParam {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "b端商家id")
    private Long shopId;
    @ApiModelProperty(value = "店铺类型 服务行业：1是服务行业  0 套餐行业")
    private Integer shopType;
    @ApiModelProperty(value = "消费方式（0用户到店，1商家上门）")
    private Integer consumptionWay;
    @ApiModelProperty(value = "订单的最终价格")
    private BigDecimal orderAllPrice;

    @ApiModelProperty(value = "商家地址（用户到店）")
    private String shippingAddress;
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

    @ApiModelProperty(value = "0商品1服务2套餐")
    private Integer typeByGoods;
    @ApiModelProperty(value = "商品服务套餐的id")
    private Long goodsId ;
    @ApiModelProperty(value = "商品服务套餐的数量")
    private  Integer number;

}
