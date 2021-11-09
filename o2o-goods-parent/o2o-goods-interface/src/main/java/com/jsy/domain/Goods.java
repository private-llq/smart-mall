package com.jsy.domain;

import java.math.BigDecimal;
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
 *  商品表
 * </p>
 *
 * @author lijin
 * @since 2021-11-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_goods")
@ApiModel(value="Goods对象", description=" 商品表")
public class Goods extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "商品图片1-5张/服务宣传图、视频")
    private String images;

    @ApiModelProperty(value = "商品分类id(服务分类)")
    private Long goodsTypeId;

    @ApiModelProperty(value = "商品名称/服务标题")
    private String title;


    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "0:普通商品 1：服务类商品")
    private Integer type;

    @ApiModelProperty(value = "服务特点表ids 逗号隔开")
    private String serviceCharacteristicsIds;

    @ApiModelProperty(value = "服务时间1  星期+时间段")
    private String serviceTime1;

    @ApiModelProperty(value = "服务时间2  星期+时间段")
    private String serviceTime2;

    @ApiModelProperty(value = "服务的价格策略  0:输入价格，展示价格  1：不对价格作展示")
    private Integer priceStrategy;

    @ApiModelProperty(value = "文字介绍")
    private String textDescription;

    @ApiModelProperty(value = "图片介绍: 0-5张 以逗号分割")
    private String photoDescription;

    @ApiModelProperty(value = "电话")
    private String phone;


}
