
package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

/**
 *
 * @author lijin
 * @since 2020-11-20
 */
@Data
public class CommonRedpacketQuery extends BaseQuery{
    /**
     * 是否有效 0-待生效 1生效 2已失效
     */
    private Integer deleted;
}