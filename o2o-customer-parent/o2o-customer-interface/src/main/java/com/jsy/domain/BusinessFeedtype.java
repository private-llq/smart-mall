package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_business_feedtype")
public class BusinessFeedtype implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty(name = "id", value = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 反馈类型
     */
    @ApiModelProperty(name = "feedType", value = "反馈类型")
    private String feedType;

    /**
     * 反馈类型id
     */
    @ApiModelProperty(name = "typeId", value = "反馈类型id")
    private Integer typeId;
    /**
     * 反馈类型uuid
     */
    @ApiModelProperty(name = "typeUuid", value = "反馈类型uuid")
    private String typeUuid;


}
