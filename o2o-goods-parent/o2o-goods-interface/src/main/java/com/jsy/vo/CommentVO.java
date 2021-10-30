package com.jsy.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("商品评论vo")
public class CommentVO {

    /**
     * 商品uuid
     */
    @ApiModelProperty(value = "商品uuid",name = "goodsUuid")
    private String goodsUuid;

    @ApiModelProperty(value = "店铺uuid",name = "shopUuid")
    private String shopUuid;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称",name = "goodsName")
    private String goodsName;

    /**
     * 商品评价0:踩,1:赞
     */
    @ApiModelProperty(value = "评价",name = "star")
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
    @ApiModelProperty(value = "评价内容",name = "content")
    private String content;

    /**
     * 上传图片地址，以逗号隔开
     */
    @ApiModelProperty(value = "上传图片地址，以逗号隔开",name = "pics")
    private String pics;

    /**
     * 评论用户头像
     */
    @ApiModelProperty(value = "评论用户头像，以逗号隔开",name = "memberIcon")
    private String memberIcon;

    @ApiModelProperty("订单uuid")
    private String orderUuid;
}
