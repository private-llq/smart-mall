package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

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
@TableName("w_set_menu_goods")
@ApiModel(value="SetMenuGoods对象", description="")
public class SetMenuGoods extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "套餐id")
    private Long setMenuId;

    @ApiModelProperty(value = "菜品名称")
    private String goodsIds;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "数量")
    private Integer amount;

    @ApiModelProperty(value = "备注")
    private String goodsExplain;



}
