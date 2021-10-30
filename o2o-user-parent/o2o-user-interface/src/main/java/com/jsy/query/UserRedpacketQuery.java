
package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author lijin
 * @since 2020-11-16
 */
@Data
public class UserRedpacketQuery extends BaseQuery{

    private LocalDateTime usedTime1;

    private LocalDateTime usedTime2;

    private String shopUuid;
}