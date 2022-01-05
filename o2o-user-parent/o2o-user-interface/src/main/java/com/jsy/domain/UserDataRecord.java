package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 用户健康信息档案
 * </p>
 *
 * @author yu
 * @since 2022-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_user_data_record")
@ApiModel(value="UserDataRecord对象", description="用户健康信息档案")
public class UserDataRecord extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;



    @ApiModelProperty(value = "分类id")
    private Long shopTreeId;

    /**
     * 分类名称
     */
    private String shopTreeName;

    @ApiModelProperty(value = "描述")
    private String description;


    @ApiModelProperty(value = "用户id")
    private String uid;

    /**
     * 消息id
     */
    private String imId;

    @ApiModelProperty(value = "1便民生活需求定制，2美食需求定制，3休闲娱乐需求定制，4社区医疗需求定制")
    private Integer type;


}
