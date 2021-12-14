package com.jsy.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


// TODO: 2020/12/16 组长已做 comment无用
@ApiModel("商家查看评论dto")
public class ShopCommentDTO {
    /**
     * 商品id
     */
    @ApiModelProperty(name = "goodsId",value = "商品id")
    private Long goodsId;
    /**
     * 商品名称
     */
    @ApiModelProperty(name = "goodsName",value = "商品名称")
    private String goodsName;

    @ApiModelProperty(name = "taste",value = "口味评价")
    private Integer taste;

    @ApiModelProperty(name = "pack",value = "包装评价")
    private Integer pack;

    @ApiModelProperty(name = "pack",value = "总体评价",notes = "1:非常差;2:差;3:一般;4:满意;5:非常满意")
    private Integer overallEvaluation;
    /**
     * 评价0:踩,1:赞
     */
    @ApiModelProperty(name = "star",value = "评价")
    private Integer star;

    /**
     * 评价内容
     */
    @ApiModelProperty(name = "content",value = "评价内容")
    private String content;

    /**
     * 上传图片地址，以逗号隔开
     */
    @ApiModelProperty(name = "pics",value = "评价图片")
    private String pics;

    // TODO: 2020/12/16 暂停修改
    private String userHead; //用户头像
    private String userNickName; //用户名称

}
