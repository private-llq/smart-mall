package com.jsy.domain;

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
@EqualsAndHashCode(callSuper = false)
@TableName("t_goods_other_cost")
@ApiModel("商品其他收费实体类")
@AllArgsConstructor
@NoArgsConstructor
public class GoodsOtherCost implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 商品其他收费id
     */
    @ApiModelProperty(value = "商品其他收费id",name = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品其他收费
     */
    @ApiModelProperty(value = "商品其他收费uuid",name = "uuid")
    private String uuid;
    /**
     * 商品其他收费名称
     */
    @ApiModelProperty(value = "商品其他收费名称",name = "name")
    private String name;
    /**
     * 商品其他收费金额
     */
    @ApiModelProperty(value = "商品其他收费金额",name = "price")
    private BigDecimal price;
    /**
     * 商品uuid
     */
    @ApiModelProperty(value = "商品uuid",name = "goodsUuid")
    private String goodsUuid;

}
