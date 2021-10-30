
package com.jsy.query;
import com.jsy.basic.util.query.BaseQuery;
/**
 *
 * @author yu
 * @since 2020-12-17
 */

public class ShopAssetsQuery extends BaseQuery{

    private String turnType;

    public String getTurnType() {
        return turnType;
    }

    public void setTurnType(String turnType) {
        this.turnType = turnType;
    }
}