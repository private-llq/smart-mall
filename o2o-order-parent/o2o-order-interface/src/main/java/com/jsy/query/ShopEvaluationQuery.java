
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;


@Data
public class ShopEvaluationQuery extends BaseQuery{

    private Long id;

    /**
     * 店铺id
     */
    private String shopUuid;
    /**
     *
     */
    private String userUuid;

    /**
     * 评价星级，1星，2星，3星，4星，5星
     */
    private Integer evaluateLevel;

}