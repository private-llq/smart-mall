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
 * 
 * </p>
 *
 * @author Tian
 * @since 2021-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_feedback")
@ApiModel(value="Feedback对象", description="")
public class Feedback extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "投诉图片")
    private String images;

    @ApiModelProperty(value = "问题")
    private String problem;

    @ApiModelProperty(value = "是否处理")
    private Integer state;



}
