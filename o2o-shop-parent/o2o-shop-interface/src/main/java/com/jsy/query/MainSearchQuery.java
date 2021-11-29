package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;


@Data
public class MainSearchQuery extends BaseQuery {

    /**
     * 关键字
     */
    private String keyword ;

    /**
     * 定位地址  经度
     */
    private String  longitude;
    /**
     * 定位地址 维度
     */

    private String  latitude;
}
