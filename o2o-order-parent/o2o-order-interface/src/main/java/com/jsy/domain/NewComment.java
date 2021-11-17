package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 评论表
 * </p>
 *
 * @author arli
 * @since 2021-11-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_new_comment")
@ApiModel(value="NewComment对象", description="评论表")
public class NewComment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "店铺id")
    private String shopId;

    @ApiModelProperty(value = "用户uuid")
    private String userId;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "订单id")
    private String orderId;

    @ApiModelProperty(value = "图片用;隔开")
    private String images;

    @ApiModelProperty(value = "评价信息")
    private String evaluateMessage;

    @ApiModelProperty(value = "评价星级，1星，2星，3星，4星，5星")
    private Integer evaluateLevel;




}
