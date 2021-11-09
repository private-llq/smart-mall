package com.jsy.param;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 档案分类表
 * </p>
 *
 * @author Tian
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Record对象", description="档案分类参数接收表")
public class RecordParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "档案分类id")
    private Long id;

    @ApiModelProperty(value = "档案分类名称")
    private String name;



}
