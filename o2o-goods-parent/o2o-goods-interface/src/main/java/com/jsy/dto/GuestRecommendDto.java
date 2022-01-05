package com.jsy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class GuestRecommendDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    @ApiModelProperty(value = "店铺分类id")
    private Long treeId;

    /**
     * 店铺分类名称
     */
    private String treeName;

    @ApiModelProperty(value = "详情说明")
    private String details;

    @ApiModelProperty(value = "图片")
    private String images;

    /**
     * 商店id
     */
    private Long shopId;
    /**
     * 商店名称
     */
    private String shopName;
    /**
     * 用户昵称
     */
    private String linkman;

    /**
     * 店铺用户id
     */
    private Long shopUserId;

    /**
     * 推荐给用户的次数
     */
    private Long userNum;



}
