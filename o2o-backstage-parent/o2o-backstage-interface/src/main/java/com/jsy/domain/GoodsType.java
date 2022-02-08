package com.jsy.domain;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@TableName("w_goods_type")
public class GoodsType {

    private static final long serialVersionUID = 1L;

    @ExcelProperty("分类id")
    @JsonSerialize(using = com.fasterxml.jackson.databind.ser.std.ToStringSerializer.class)
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "行业服务分类名称",name = "classifyName")
    @ExcelProperty("分类名称")
    private String classifyName;

    /**
     * 等级
     */
    @ExcelProperty("等级")
    @ApiModelProperty(value = "等级",name = "level")
    private Integer level;

    /**
     * 父级id
     */
    @ApiModelProperty(value = "父级id",name = "pid")
    @ExcelIgnore
    private Long pid;

    /**
     * 是否显示
     */
    @ApiModelProperty(value = "是否显示",name = "state")
    @ExcelIgnore
    private Integer state;


    @TableField(exist = false)
    @ApiModelProperty(value = "子节点列表")
    @ExcelIgnore
    private List<GoodsType> children;

}
