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
@TableName("t_shop_type")
@ApiModel("店铺类型实体类")
public class ShopType implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 店铺类型id
     */
    @ApiModelProperty(value = "店铺类型id",name = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 店铺类型uuid
     */
    @ApiModelProperty(value = "店铺类型uuid",name = "uuid")
    private String uuid;

    /**
     * 类型名称
     */
    @ApiModelProperty(value = "店铺类型名称",name = "name")
    private String name;

    /**
     * 店铺类型图片
     */
    @ApiModelProperty(value = "店铺类型图片",name = "imageUrl")
    private String imageUrl;
    /**
     * 父类类型id 0为顶级分类
     */
    @ApiModelProperty(value = "父类类型id",name = "parentId",notes = "0为顶级分类")
    private Long parentId;
    /**
     * 最大父级为0；
     * 格式为：最大父级id-父级id ...
     */
    @ApiModelProperty(value = "父级等级树",name = "typeLevel")
    private String typeLevel;

    @ApiModelProperty(value = "父级类型uuid",name = "parentUuid")
    private String parentUuid;


}
