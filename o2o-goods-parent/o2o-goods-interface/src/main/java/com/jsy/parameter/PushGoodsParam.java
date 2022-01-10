package com.jsy.parameter;

import lombok.Data;

import java.io.Serializable;

@Data
public class PushGoodsParam implements Serializable {
    /**
     * 商品id
     */
    private Long id;
    /**
     * 推送位置 0 未推送（数据库默认0）  1：医疗  2 ：养老 3 商城
     */
    private Integer type;

    /**
     * 设置数据排序位置
     */
    private Long sort;
}
