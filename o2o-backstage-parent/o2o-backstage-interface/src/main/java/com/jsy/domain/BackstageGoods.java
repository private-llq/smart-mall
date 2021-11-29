package com.jsy.domain;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Tian
 * @since 2021-11-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_backstage_goods")
public class BackstageGoods extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商品分类id")
    private Long goodsTypeId;

    @ApiModelProperty(value = "商品标题")
    private String title;

    @ApiModelProperty(value = "副标题")
    private String subTitle;

    @ApiModelProperty(value = "产品编号")
    private String goodsNumber;

    @ApiModelProperty(value = "关键词")
    private String keyword;

    @ApiModelProperty(value = "商品图片")
    private String images;

    @ApiModelProperty(value = "详细内容")
    private String detail;

    @ApiModelProperty(value = "品牌")
    private BigDecimal brand;

    @ApiModelProperty(value = "展示价格")
    private BigDecimal showPrice;

    @ApiModelProperty(value = "市场价格")
    private BigDecimal marketPrice;

    @ApiModelProperty(value = "0 禁用 1 启用")
    private Integer state;

    @ApiModelProperty(value = "排序")
    private Long sort;

    @ApiModelProperty(value = "发布平台")
    private String platform;


}
