package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

/**
 *
 * @author lijin
 * @since 2020-11-12
 */
@Data
public class ShopOwnerQuery extends BaseQuery {
    /**
     * 姓名
     */
    private String name;
    /**
     * 电话号码
     */
    private String phone;
}