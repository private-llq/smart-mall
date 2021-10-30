package com.jsy.dto;

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


@Data
@ApiModel("商品规格实体类")
@AllArgsConstructor
@NoArgsConstructor
public class GoodsSpecDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "规格id",name = "id")


    private String uuid;

    @ApiModelProperty(value = "商品uuid",name = "goodsUuid")
    private String goodsUuid;


    @ApiModelProperty(value = "规格名称",name = "specName")
    private String specName;

    @ApiModelProperty(value = "规格价格",name = "specPrice")
    private BigDecimal specPrice;


    @ApiModelProperty(value = "规格状态",name = "specStatus",notes = "0:正常;1:失效")
    private String specStatus;

    @ApiModelProperty(value = "规格选项",name = "specOptions")
    private String specOptions;


    @ApiModelProperty(value = "规格图片",name = "specImages")
    private String specImages;


}
