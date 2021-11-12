package com.jsy.parameter;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
@ApiModel(value="SetMenuGoods对象", description="")
public class SetMenuGoodsParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "套餐id")
    private Long setMenuId;

    @ApiModelProperty(value = "菜品ids")
    private String goodsIds;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "数量")
    private Integer amount;

    @ApiModelProperty(value = "备注")
    private String goodsExplain;
//
//    @ApiModelProperty(value = "菜品名称")
//    @TableField(exist = false)
//    private String name;
//
//    @TableField(exist = false)
//    @ApiModelProperty(value = "菜品价格")
//    private BigDecimal price;
//
//    @TableField(exist = false)
//    private  Map<String, List<SetMenuGoodsParam>> map;


}
