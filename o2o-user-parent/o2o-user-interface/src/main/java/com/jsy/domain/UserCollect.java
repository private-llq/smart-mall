package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yu
 * @since 2021-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_user_collect")
@ApiModel(value="UserCollect对象", description="")
public class UserCollect extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "套餐id")
    private Long menuId;
    /**
     * 收藏类型：0 商品、服务 1：套餐 2：商店 3：服务
     */
    private Integer type;

    /**
     * 店铺评分
     */
    private Double shopScore;
    /**
     * 店铺类型名称
     */
    private String shopTypeName;
    /**
     * 商品/服务/套餐/店铺标题
     */
    private String title;
    /**
     * 商品/服务/套餐/店铺图片
     */
    private String image;
    /**
     * 商品/服务/套餐价格
     */
    private BigDecimal price;
    /**
     * 商品/服务/套餐折扣价格
     */
    private BigDecimal discountPrice;

    /**
     * 折扣状态
     */
   private Integer discountState;





}
