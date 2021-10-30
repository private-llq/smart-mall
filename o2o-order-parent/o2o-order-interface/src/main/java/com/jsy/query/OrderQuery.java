
package com.jsy.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
public class OrderQuery extends BaseQuery implements Serializable{

    private String userUuid;
    //创建时间
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime createTime1;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime createTime2;
    //订单编号
    private Long orderNum;
    //0今天 1昨天 7七天内 30三十天内
    private int dayType;

    private String shopUuid;
    //stateId 3:待评价
    private Integer stateId =-1;
    //used 0是待使用
    private String used;
    //paystate 0:待付款  2是退款中 3是退款
    private String payState;
    //evaluation_id  -1待评价  1已评价
    private Integer evaluationId;

    private Integer orderState;
}