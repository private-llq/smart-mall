package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author lijin
 * @since 2020-11-12
 */
@Data
@ApiModel("店铺搜索")
public class ShopInfoQuery extends BaseQuery implements Serializable {

    @ApiModelProperty("经度")
    private BigDecimal longitude;

    @ApiModelProperty("纬度")
    private BigDecimal latitude;

    @ApiModelProperty("排序规则")
    private int order;

    @ApiModelProperty("搜索半径,单位为千米")
    private Integer radius = 5;

    @ApiModelProperty("分页偏移量")
    private Integer limit;

    @ApiModelProperty("店铺类型id")
    private Long id;

    @ApiModelProperty("筛选对象")
    private ShopScreen shopScreen;

}