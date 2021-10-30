
package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author yu
 * @since 2020-12-11
 */
@Data
public class SuggestionsQuery extends BaseQuery {

    private String uuid;
    private LocalDateTime createTime;
    private Integer isdeal;
    private LocalDateTime finishTime;
    private String dealmanUuid;

}