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

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_goods_property")
@ApiModel("商品属性实体类")
@AllArgsConstructor
@NoArgsConstructor
public class GoodsProperty implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 商品其他收费id
     */
    @ApiModelProperty(value = "商品属性id",name = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 商品属性uuid
     */
    @ApiModelProperty(value = "商品属性uuid",name = "uuid")
    private String uuid;
    /**
     * 商品uuid
     */
    @ApiModelProperty(value = "商品uuid",name = "goodsUuid")
    private String goodsUuid;
    /**
     * 商品的属性名
     */
    @ApiModelProperty(value = "商品的属性名",name = "propertyName")
    private String propertyName;

    /**
     * 商品的属性的细分类
     */
    @ApiModelProperty(value = "商品的属性的细分类",name = "propertyClass")
    private String propertyClass;
}
