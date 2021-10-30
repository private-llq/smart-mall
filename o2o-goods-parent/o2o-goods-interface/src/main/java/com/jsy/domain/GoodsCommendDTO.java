package com.jsy.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class GoodsCommendDTO {
    private String title;
    private String images;
    private BigDecimal price;
    private BigDecimal discountPrice;
    private String shopUuid;
    private int sales;
}
