
package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 *
 * @author lijin
 * @since 2021-03-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentReplayQuery extends BaseQuery{


    private LocalDateTime createTime1;

    private LocalDateTime createTime2;
}