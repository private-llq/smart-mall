
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

/**
 *
 * @author yu
 * @since 2021-11-22
 */

@Data
public class UserCollectQuery extends BaseQuery{
    /**
     * 用户id
     */
    private String userId;

    /**
     * 收藏类型 0 商品、服务 1 套餐 2 商店
     */
    private Integer type;
}