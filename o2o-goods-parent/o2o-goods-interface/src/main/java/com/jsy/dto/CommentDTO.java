package com.jsy.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@ApiModel("评论DTO")
public class CommentDTO {

    @ApiModelProperty(name = "id",value = "评论id")
    private Long id;
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

    @ApiModelProperty(name = "shopName",value = "店铺名称")
    private String shopName;

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
     * 创建时间
     */
    @ApiModelProperty(name = "createTime",value = "创建时间")
    private LocalDateTime createTime;

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

    @ApiModelProperty(name = "shopLogo",value = "店铺logo图片")
    private String shopLogo;


}
