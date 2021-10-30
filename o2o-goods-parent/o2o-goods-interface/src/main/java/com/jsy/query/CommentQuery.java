
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

/**
 *
 * @author lijin
 * @since 2020-12-01
 */
@Data
public class CommentQuery extends BaseQuery{

    //查看其他用户评论的uuid
    private String uuid;
}