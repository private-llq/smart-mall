package com.jsy.domain;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class Goods extends BaseEntity  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品编号")
    private String goodsNumber;

    @JSONField(serializeUsing = ToStringSerializer.class)
    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "商家name")
    private String shopName;

    @ApiModelProperty(value = "商品/服务 - 图片1-3张")
    private String images;

    @ApiModelProperty(value = "商品/服务 分类id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long goodsTypeId;

    @ApiModelProperty(value = "商品/服务 分类name")
    private String goodsTypeName;

    @ApiModelProperty(value = "商品名称/服务标题")
    private String title;

    @ApiModelProperty(value = "上下架状态 0:下架 1：上架")
    private Integer isPutaway;

    @ApiModelProperty(value = "商品价格")
    private BigDecimal price;

    @ApiModelProperty(value = "是否开启折扣：0未开启 1开启")
    private Integer discountState;

    @ApiModelProperty(value = "商品折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "0:普通商品 1：服务类商品")
    private Integer type;

   /* @ApiModelProperty(value = "服务特点表ids 逗号隔开")
    private String serviceCharacteristicsIds;*/

//    @ApiModelProperty(value = "服务特点name 逗号隔开")
//    private String serviceCharacteristicsName;

    @ApiModelProperty(value = "服务的有效期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validUntilTime;

    @ApiModelProperty(value = "服务的价格策略  0:平台销售  1：宣传服务")
    private Integer priceStrategy;

    @ApiModelProperty(value = "商品的说明/服务的备注")
    private String textDescription;

    @ApiModelProperty(value = "服务的电话")
    private String serviceCall;

    @ApiModelProperty(value = "服务的使用规则")
    private String serviceRegulations;

  /*  @ApiModelProperty(value = "是否支持上门服务 0 不支持 1 支持")
    private Integer isVisitingService;*/

    @ApiModelProperty(value = "访问量")
    private Long pvNum;

    @ApiModelProperty(value = "大后台：商品状态 0 启用 1禁用")
    private Integer state;

    /**
     * 虚拟销量状态 0：关闭 1：开启
     */
    private Integer virtualState;

    /**
     * 虚拟销量
     */
    private Long virtualSales;

    /**
     * 推送状态：0 未推送  1：医疗  2 ：养老 3 商城
     */
    private Integer pushState;

    /**
     * 是否是官方商品 0不是 1是
     */
    private Integer isOfficialGoods;

    @TableField(exist = false)
    @ApiModelProperty(value = "销量")
    private Long sums=0L;



}
