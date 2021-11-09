package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@TableName("w_record")
@ApiModel(value="Record对象", description="档案分类表")
public class Record extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "档案分类名称")
    private String name;



}
