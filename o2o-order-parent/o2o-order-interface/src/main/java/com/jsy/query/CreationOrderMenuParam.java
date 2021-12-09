package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @className（类名称）: CreationOrderMenuParam
 * @description（类描述）: this is the CreationOrderMenuParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/15
 * @version（版本）: v1.0
 */
@Data
public class CreationOrderMenuParam implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "套餐名称")
    private String name;

    @ApiModelProperty(value = "套餐id")
    private Long menuId;

    @ApiModelProperty(value = "原价")
    private BigDecimal realPrice;

    @ApiModelProperty(value = "折扣价")
    private BigDecimal sellingPrice;

    @ApiModelProperty(value = "是否开启折扣0未开启,1是开启")
    private Integer discountState;

    @ApiModelProperty(value = "图片（最大三张）")
    private String images;

    @ApiModelProperty(value = "使用规则")
    private String rule;

    @ApiModelProperty(value = "套餐说明")
    private String menuExplain;

    @ApiModelProperty(value = "数量")
    private Integer amount;

    @ApiModelProperty(value = "套餐的详情")
    private List<CreationOrderMenuGoodsParam> creationOrderMenuGoodsParams;

}
