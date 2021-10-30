
package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderCommodityQuery extends BaseQuery{


    private String goodsUuid;

    private LocalDateTime createTime1;

    private LocalDateTime createTime2;

}