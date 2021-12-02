package com.jsy.query;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

//    @ApiModelProperty(value = "服务特点表ids 逗号隔开")
//    private String serviceCharacteristicsIds;

    @ApiModelProperty(value = "原价")
    private BigDecimal realPrice;

    @ApiModelProperty(value = "折扣价")
    private BigDecimal sellingPrice;

    @ApiModelProperty(value = "图片（最大三张）")
    private String images;

    @ApiModelProperty(value = "开始有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime indateStart;

    @ApiModelProperty(value = "截止有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime indateEnd;

    @ApiModelProperty(value = "使用规则")
    private String rule;

    @ApiModelProperty(value = "套餐说明")
    private String menuExplain;

    @ApiModelProperty(value = "数量")
    private Integer amount;

    @ApiModelProperty(value = "套餐的详情")
    private List<CreationOrderMenuGoodsParam> creationOrderMenuGoodsParams;



}
