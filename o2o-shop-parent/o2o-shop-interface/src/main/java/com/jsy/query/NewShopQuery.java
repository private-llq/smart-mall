
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

/**
 *
 * @author yu
 * @since 2021-11-08
 */
@Data
public class NewShopQuery extends BaseQuery{
    /**
     * 定位地址
     */
    private String  location;
    /**
     * 分类id
     */
    private Long treeId;
}