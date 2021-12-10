
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author yu
 * @since 2021-11-22
 */
@Data
public class UserAddrQuery extends BaseQuery{
    /**
     * 经度
     */
    private BigDecimal longitude;
    /**
     * 维度
     */
    private BigDecimal latitude;

}