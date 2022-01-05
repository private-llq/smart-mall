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
 * 
 * </p>
 *
 * @author Tian
 * @since 2022-01-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_guest_recommend")
@ApiModel(value="GuestRecommend对象", description="")
public class GuestRecommend extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 推荐给用户的数量
     */
    private Long userNum;


}
