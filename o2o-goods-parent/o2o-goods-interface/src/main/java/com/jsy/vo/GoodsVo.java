package com.jsy.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.jsy.domain.GoodsSpec;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(value = "商品保存vo")
@Data
public class GoodsVo {
    /**
     * 商品uuid
     */
    @ApiModelProperty(value = "商品uuid",name = "uuid")
    private String uuid;
    /**
     * 商品类型id
     */
    @ApiModelProperty(value = "商品类型uuid",name = "goodsTypeUuid")
    private String goodsTypeUuid;
    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格",name = "price")
    private BigDecimal price;

    /**
     * 商品折扣价格
     */
    @ApiModelProperty(value = "商品折扣价格",name = "discountPrice")
    private BigDecimal discountPrice;
    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品标题",name = "title")
    private String title;
    /**
     * 商品描述
     */
    @ApiModelProperty(value = "商品描述",name = "ownSpec")
    private String ownSpec;
    /**
     * 是否启用规格0:否；1:启用
     */
    @ApiModelProperty(value = "是否启用规格",name = "isEnableSpec",notes = "0:不启用;1:启用")
    private String isEnableSpec;
    /**
     * 商品图片
     */
    @ApiModelProperty(value = "商品图片地址",name = "imagesUrl")
    private String imagesUrl;

    /**
     * 库存
     */
    @ApiModelProperty(value = "商品库存",name = "stock")
    private Integer stock;

    /**
     * 商品的规格
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "商品规格对象集合",name = "specList")
    private List<GoodsSpecUpdateVo> specList;

}
