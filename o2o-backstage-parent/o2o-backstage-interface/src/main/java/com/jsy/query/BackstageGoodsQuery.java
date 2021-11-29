
package com.jsy.query;
import com.alibaba.fastjson.annotation.JSONField;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author Tian
 * @since 2021-11-29
 */

@Data
public class BackstageGoodsQuery extends BaseQuery{

    /**
     * 商品分类id
     */
    private Long goodsTypeId;
    /**
     * 商品编号
     */
    private String goodsNumber;
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

    /**
     * 销量 0 ：降序 1 升序
     */
    private Integer salesSort;

    /**
     * 价格 0 ：降序 1 升序
     */
    private Integer priceSort;
}