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
import java.time.LocalDateTime;

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
@ApiModel(value="UserRecord对象", description="用户个人档案对象接收对象")
public class UserRecordParam implements Serializable {


    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "档案名称")
    private String name;

    @ApiModelProperty(value = "档案详情")
    private String details;

    @ApiModelProperty(value = "档案分类id")
    private Long recordId;

    @ApiModelProperty(value = "用户id")
    private Long userId;




}
