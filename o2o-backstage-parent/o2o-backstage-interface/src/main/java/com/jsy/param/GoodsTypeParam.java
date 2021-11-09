package com.jsy.param;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
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
public class GoodsTypeParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "行业服务分类名称",name = "classifyName")
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


}
