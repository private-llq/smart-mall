
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author yu
 * @since 2021-12-01
 */
@Data
public class ShopAuditQuery extends BaseQuery{

    @ApiModelProperty(value = "审核状态 0未审核 1已审核 2审核未通过 3资质未认证")
    private Integer state;

    @ApiModelProperty(value = "屏蔽状态 0未屏蔽  1已屏蔽")
    private Integer shielding;

}