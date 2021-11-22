
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
import com.jsy.basic.util.query.PageQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author yu
 * @since 2021-11-22
 */
@Data
public class BrowseQuery extends BaseQuery {
    @ApiModelProperty(value = "用户id")
    private Long userId;
}