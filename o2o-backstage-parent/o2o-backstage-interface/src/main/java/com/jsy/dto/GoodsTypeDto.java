package com.jsy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

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
public class    GoodsTypeDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "行业服务分类名称",name = "classifyName")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;
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
    private List<GoodsTypeDto> children;

}
