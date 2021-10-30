package com.jsy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@Data
@ApiModel("修改商品规格实体类vo")
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSpecUpdateVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 商品规格uuid
     */
    @ApiModelProperty(value = "规格uuid",name = "uuid")
    private String uuid;


    /**
     * 规格名称
     */
    @ApiModelProperty(value = "规格名称",name = "specName")
    private String specName;
    /**
     * 价格
     */
    @ApiModelProperty(value = "规格价格",name = "specPrice")
    private BigDecimal specPrice;
    /**
     * 规格选项
     */
    @ApiModelProperty(value = "规格选项",name = "specOptions")
    private String specOptions;
    /**
     * 状态;0:正常；1:失效
     */
    @ApiModelProperty(value = "规格状态",name = "specStatus",notes = "0:正常;1:失效")
    private String specStatus;
    /**
     * 规格图片
     */
    @ApiModelProperty(value = "规格图片",name = "specImages")
    private String specImages;


}
