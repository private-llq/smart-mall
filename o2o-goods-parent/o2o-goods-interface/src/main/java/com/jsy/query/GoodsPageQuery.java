package com.jsy.query;

import com.jsy.basic.util.query.PageQuery;
import lombok.Data;


@Data
public class GoodsPageQuery extends PageQuery {


    /**
     * 商店id
     */
    private Long shopId;

    /**
     * 上下架状态 0 未上架 1 上架  不传默认查询全部
     */
    private Integer isPutaway;

    /**
     * 0:普通商品 1：服务类商品
     */
    private Integer Type;

    /**
     * 大后台：商品状态 0 启用 1禁用
     */
    private Integer state;


}
