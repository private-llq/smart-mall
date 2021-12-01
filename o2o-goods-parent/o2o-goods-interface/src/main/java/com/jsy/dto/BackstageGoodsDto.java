package com.jsy.dto;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BackstageGoodsDto implements Serializable {

    private Long id;

    /**
     * 商品名称
     */
    private String title;
    /**
     * 分类名称
     */
    private String goodsTypeName;
    /**
     * 发布商户
     */
    private String shopName;
    /**
     * 发布时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 上下架状态 0:下架 1：上架
     */
    private Integer isPutaway;
    /**
     * 商品状态 0 启用 1禁用
     */
    private Integer state;
}
