
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author yu
 * @since 2020-12-24
 */
@Data
public class OrderLogQuery extends BaseQuery{

    private String type;
    private LocalDateTime createTime1;
    private LocalDateTime createTime2;
    private String requestIp;
    private String finishPersonUuid;
}