package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_goods_basic")
@ApiModel("商品实体类")
public class  GoodsBasic implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "商品id",name = "id")
    private Long id;

    /**
     * 商品uuid
     */
    @ApiModelProperty(name = "uuid",value = "商品uuid")
    private String uuid;

    /**
     * 商品类型id
     */
    @ApiModelProperty(value = "商品类型uuid",name = "goodsTypeUuid")
    private String goodsTypeUuid;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺uuid",name = "shopUuid")
    private String shopUuid;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格",name = "price")
    private BigDecimal price;

    @ApiModelProperty(value = "商品折扣价格",name = "discountPrice")
    private BigDecimal discountPrice;
    /**
     * 商品状态0:失效；1:正常
     */
    @ApiModelProperty(value = "商品状态",name = "status",notes = "0:正常;1:失效")
    private String status;
    /**
     * 商品详情
     */
    @ApiModelProperty(value = "商品详情",name = "ownSpec")
    private String ownSpec;


    /**
     * 是否上架0:否；1:上架
     */
    @ApiModelProperty(value = "商品是否上架",name = "isMarketable",notes = "0:否;1:上架")
    private String isMarketable;
    /**
     * 商品标题
     */


    @ApiModelProperty(value = "商品标题",name = "title")
    private String title;


    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号",name = "goodNumber")
    private String goodNumber;

    @TableField(exist = false)
    @ApiModelProperty(value = "折扣活动进行状态（1正在进行0已经失效）",name = "discountIngStatus")
    private Integer discountIngStatus;
    /**
     * 是否启用规格0:否；1:启用
     */
    @ApiModelProperty(value = "是否启用规格",name = "isEnableSpec",notes = "0:不启用;1:启用")
    private String isEnableSpec;

    /**
     * 商品图片ids
     */
    @ApiModelProperty(value = "商品图片地址",name = "imagesUrl")
    private String imagesUrl;

    /**
     * 库存
     */
    @ApiModelProperty(value = "商品库存",name = "stock")
    private Integer stock;

    /**
     * 商品销量
     */
    @ApiModelProperty(value = "商品销量",name = "sales")
    private Integer sales;
    /**
     * 排序序号
     */
    @ApiModelProperty(value = "排序序号",name = "ranks")
    private Integer ranks;

    /**
     * 海报排序 序号
     */
    @ApiModelProperty(value = "排序序号",name = "sort")
    private Integer sort;

    /**
     * 商品的折扣(0.10-0.99)
     */
    @ApiModelProperty(value = "商品的折扣(0.10-0.99)",name = "discount")
    private Double discount;

    /**
     * 折扣开始时间
     */
    @ApiModelProperty(value = "折扣开始时间",name = "discountStartTime")
    private LocalDateTime discountStartTime;

    /**
     * 折扣结束时间
     */
    @ApiModelProperty(value = "折扣结束时间",name = "discountEndTime")
    private LocalDateTime discountEndTime;

    /**
     * 限购份数
     */
    @ApiModelProperty(value = "限购份数",name = "astrictNumber")
    private Integer astrictNumber;

    /**
     * 折扣活动状态(1启用0未启动)
     */
    @ApiModelProperty(value = "折扣活动状态",name = "activityStatus")
    private Integer activityStatus;

    /**
     * 1按照折扣0按照折扣价格
     */
    @ApiModelProperty(value = "1按照折扣0按照折扣价格",name = "discountStatus")
    private Integer discountStatus;

    @TableField(exist = false)
    @ApiModelProperty(name = "num")
    private Integer num;

}
