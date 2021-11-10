package com.jsy.domain;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.List;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2021-11-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_set_menu")
@ApiModel(value="SetMenu对象", description="")
public class SetMenu  extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "套餐名称")
    private String name;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "服务特点表ids 逗号隔开")
    private String serviceCharacteristicsIds;

    @ApiModelProperty(value = "真实价格")
    private BigDecimal realPrice;

    @ApiModelProperty(value = "售卖价格")
    private BigDecimal sellingPrice;

    @ApiModelProperty(value = "图片（最大三张）")
    private String images;

    @ApiModelProperty(value = "有效期")
    private LocalDateTime indate;

    @ApiModelProperty(value = "使用规则")
    private String rule;

    @ApiModelProperty(value = "上下架（0下架  1上架）")
    private Integer state;

    @ApiModelProperty(value = "套餐详情")
    @TableField(exist = false)
    List<SetMenuGoods> setMenuGoodsList;

    @ApiModelProperty(value = "商品 类型（0商品套餐 1服务套餐）")
    private Integer type;


}
