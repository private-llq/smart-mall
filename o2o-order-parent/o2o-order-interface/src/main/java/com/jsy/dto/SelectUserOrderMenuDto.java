package com.jsy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @className（类名称）: SelectUserOrderMenuDto
 * @description（类描述）: this is the SelectUserOrderMenuDto class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/16
 * @version（版本）: v1.0
 */
@Data
public class SelectUserOrderMenuDto extends BaseEntity {
    @ApiModelProperty(value = "订单id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long orderId;

    @ApiModelProperty(value = "套餐名称")
    private String name;

    @ApiModelProperty(value = "店铺id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    @ApiModelProperty(value = "数量")
    private Integer amount;

    @ApiModelProperty(value = "套餐id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long menuId;

    @ApiModelProperty(value = "原价")
    private BigDecimal realPrice;

    @ApiModelProperty(value = "折扣价")
    private BigDecimal sellingPrice;

    @ApiModelProperty(value = "是否开启折扣：0未开启 1开启")
    private Integer discountState;

    @ApiModelProperty(value = "图片（最大三张）")
    private String images;

//    @ApiModelProperty(value = "使用规则")
//    private String rule;

    @ApiModelProperty(value = "套餐说明")
    private String menuExplain;

    @ApiModelProperty(value = "套餐详情dto")
    private  List<SelectUserOrderMenuGoodsDto> orderMenuGoodsDtos;

}
