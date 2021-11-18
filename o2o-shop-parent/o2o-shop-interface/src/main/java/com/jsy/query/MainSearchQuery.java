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
     * 定位地址
     */
    private String  location;
}
