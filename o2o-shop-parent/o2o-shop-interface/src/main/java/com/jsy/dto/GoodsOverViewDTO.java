package com.jsy.dto;

import lombok.Data;

@Data
public class GoodsOverViewDTO {

    //上架商品数量
    private Integer onNum;

    //下架商品数量
    private Integer outNum;

    //所有商品数量
    private Integer allNum;

    //商品库存紧张数量
    private Integer lowStockNum;



}
