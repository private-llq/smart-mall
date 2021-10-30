package com.jsy.domain;

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
 * @author lijin
 * @since 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_goods_type")
@ApiModel("商品类型实体")
public class GoodsType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品类型id
     */
    @ApiModelProperty(value = "商品类型id",name = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品类型uuid
     */
    @ApiModelProperty(value = "店铺uuid",name = "uuid")
    private String uuid;

    /**
     * 店铺uuid
     */
    @ApiModelProperty(value = "店铺类型uuid",name = "shopUuid")
    private String shopUuid;
    /**
     * 排序序号
     */
    @ApiModelProperty(value = "排序序号",name = "ranks")
    private Integer ranks;
    /**
     * 商品类型名称
     */
    @ApiModelProperty(value = "商品类型名称",name = "name")
    private String name;
    /**
     * 分类描述
     */
    @ApiModelProperty(value = "分类描述",name = "details")
    private String details;
    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量",name = "num")
    private String num;
}
