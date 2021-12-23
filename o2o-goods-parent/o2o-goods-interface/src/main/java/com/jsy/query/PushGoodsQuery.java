
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author lijin
 * @since 2021-12-06
 */
@Data
public class PushGoodsQuery extends BaseQuery{
    /**
     *  排序-》   0 默认  1 销量升序  2 销量降序  3 价格升序 4 价格降序
     */
    private Integer type=0;
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 维度
     */
    private BigDecimal latitude;

}