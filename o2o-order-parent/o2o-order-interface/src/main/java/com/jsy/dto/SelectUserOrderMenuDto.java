package com.jsy.dto;

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
public class SelectUserOrderMenuDto {
    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "套餐名称")
    private String name;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "数量")
    private Integer amount;



    @ApiModelProperty(value = "套餐id")
    private Long menuId;

    @ApiModelProperty(value = "原价")
    private BigDecimal realPrice;

    @ApiModelProperty(value = "折扣价")
    private BigDecimal sellingPrice;

    @ApiModelProperty(value = "图片（最大三张）")
    private String images;

    @ApiModelProperty(value = "开始有效期")
    private LocalDateTime indateStart;

    @ApiModelProperty(value = "截止有效期")
    private LocalDateTime indateEnd;

    @ApiModelProperty(value = "使用规则")
    private String rule;

    @ApiModelProperty(value = "套餐说明")
    private String menuExplain;

    @ApiModelProperty(value = "套餐详情dto")
    private  List<SelectUserOrderMenuGoodsDto> orderMenuGoodsDtos;

}
