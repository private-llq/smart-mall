package com.jsy.domain;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2021-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_goods")
@ApiModel(value="ShopGoods对象", description="")
public class ShopGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "商品图片")
    private String images;

    @ApiModelProperty(value = "商品分类id")
    private Long classifyId;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品详情介绍")
    private String introduce;

    @ApiModelProperty(value = "价格")
    private BigDecimal money;

    @ApiModelProperty(value = "作用")
    private String effect;

    @ApiModelProperty(value = "介绍图片")
    private String introduceImages;


}
