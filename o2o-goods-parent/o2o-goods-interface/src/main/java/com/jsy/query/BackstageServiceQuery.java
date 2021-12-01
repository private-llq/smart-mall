package com.jsy.query;


import com.alibaba.fastjson.annotation.JSONField;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BackstageServiceQuery extends BaseQuery {
    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 分类id
     */
    private Long goodsTypeId;
    /**
     * 状态  0 启用 1禁用
     */
    private Integer state;

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
