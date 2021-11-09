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
 * 用户个人档案表
 * </p>
 *
 * @author yu
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_user_record")
@ApiModel(value="UserRecord对象", description="用户个人档案表")
public class UserRecord extends BaseEntity {

    @ApiModelProperty(value = "档案名称")
    private String name;

    @ApiModelProperty(value = "档案详情")
    private String details;

    @ApiModelProperty(value = "档案分类id")
    private Long recordId;

    @ApiModelProperty(value = "用户id")
    private Long userId;



}
