package com.jsy.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * </p>
 * @author lijin
 * @since 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_goods_spec")
@ApiModel("商品规格实体类")
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSpec implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 规格id
     */
    @ApiModelProperty(value = "规格id",name = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品规格uuid
     */
    private String uuid;
    /**
     * 商品uuid
     */
    @ApiModelProperty(value = "商品uuid",name = "goodsUuid")
    private String goodsUuid;
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
     * 状态;0:正常；1:失效
     */
    @ApiModelProperty(value = "规格状态",name = "specStatus",notes = "0:正常;1:失效")
    private String specStatus;
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
