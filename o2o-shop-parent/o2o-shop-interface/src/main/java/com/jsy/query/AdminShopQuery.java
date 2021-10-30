package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.io.Serializable;

@Data
public class AdminShopQuery extends BaseQuery {
    private String status;
}
