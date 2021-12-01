package com.jsy.query;

import com.alibaba.fastjson.annotation.JSONField;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BackstageGoodsQuery extends BaseQuery  {
    /**
     * 分类id
     */
    private Long goodsTypeId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 开始时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
