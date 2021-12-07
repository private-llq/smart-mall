package com.jsy.domain;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Field;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2021-12-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_push_goods")
@ApiModel(value="PushGoods对象", description="")
public class PushGoods extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商家id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long shopId;

    @ApiModelProperty(value = "冗余字段 ：商店name")
    private String shopName;

    @ApiModelProperty(value = "商品/服务 分类id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long goodsTypeId;

    @ApiModelProperty(value = "冗余字段： 商品分类name")
    private String goodsTypeName;

    @ApiModelProperty(value = "商品编号")
    private String goodsNumber;

    @ApiModelProperty(value = "商品名称/服务标题")
    private String title;

    @ApiModelProperty(value = "商品/服务 - 图片1-3张")
    private String images;

    @ApiModelProperty(value = "商品/服务 价格")
    private BigDecimal price;

    @ApiModelProperty(value = "商品/服务 折扣价格")
    private BigDecimal discountPrice;

    @ApiModelProperty(value = "商品的说明/服务的备注")
    private String textDescription;

    @ApiModelProperty(value = "排序字段")
    private Long sort;

    @ApiModelProperty(value = "销量")
    @TableField(exist = false)
    private Long sums=0L;

}
