package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.List;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 大后台 行业服务分类
 * </p>
 *
 * @author Tian
 * @since 2021-11-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_goods_type")
public class GoodsType extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /**
     * 分类名称
     */
    @ApiModelProperty(value = "行业服务分类名称",name = "classifyName")
    private String classifyName;

    /**
     * 等级
     */
    @ApiModelProperty(value = "等级",name = "level")
    private Integer level;

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id",name = "pid")
    private Long pid;

    /**
     * 是否显示
     */
    @ApiModelProperty(value = "是否显示",name = "state")
    private Integer state;


    @TableField(exist = false)
    @ApiModelProperty(value = "子节点列表")
    private List<GoodsType> children;

}
