
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author lijin
 * @since 2021-11-10
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SetMenuQuery extends BaseQuery{
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
}