package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2020-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论uuid
     */
    private String uuid;

    /**
     * 商品uuid
     */
    private String goodsUuid;

    /**
     * 会员昵称
     */
    private String userNickName;

    /**
     * 用户id
     */
    private String userUuid;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 评价0:踩,1:赞
     */
    private Integer star;

    @ApiModelProperty(name = "taste",value = "口味评价")
    private Integer taste;

    @ApiModelProperty(name = "pack",value = "包装评价")
    private Integer pack;

    @ApiModelProperty(name = "overallEvaluation",value = "总体评价",notes = "1:非常差;2:差;3:一般;4:满意;5:非常满意")
    private Integer overallEvaluation;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 上传图片地址，以逗号隔开
     */
    private String pics;

    /**
     * 评论用户头像
     */
    private String userHead;

    /**
     * 是否匿名0:不匿名；1:匿名
     */
    private String anonymity;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
