package com.jsy.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
@ApiModel("店铺类型vo")
public class ShopTypeVo {

    /**
     * 店铺类型id
     */
    @ApiModelProperty(value = "店铺类型id",name = "id")
    private Long id;

    @ApiModelProperty(name = "uuid",value = "店铺类型uuid")
    private String uuid;

    /**
     * 类型名称
     */
    @ApiModelProperty(value = "店铺类型名称",name = "name")
    private String name;

    /**
     * 父类类型id 0为顶级分类
     */
    @ApiModelProperty(value = "父类类型id",name = "parentId",notes = "0为顶级分类")
    private Long parentId;

    /**
     * 店铺类型图片
     */
    @ApiModelProperty(value = "店铺类型图片",name = "imageUrl")
    private String imageUrl;


    /**
     * 类型子类
     */
    @ApiModelProperty(value = "类型子类对象集合",name = "children")
    private List<ShopTypeVo> children;
}
