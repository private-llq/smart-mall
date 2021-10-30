package com.jsy.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@ApiModel("add商品规格实体类vo")
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSpecVo implements Serializable {
    private static final long serialVersionUID = 1L;
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
     * 规格图片
     */
    @ApiModelProperty(value = "规格图片",name = "specImages")
    private String specImages;


}
