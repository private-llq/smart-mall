package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author lijin
 * @since 2020-11-12
 */
@Data
public class GoodsBasicQuery extends BaseQuery{

    @ApiModelProperty(value = "商品推荐规则",notes = "传入1")
    private String order;
}