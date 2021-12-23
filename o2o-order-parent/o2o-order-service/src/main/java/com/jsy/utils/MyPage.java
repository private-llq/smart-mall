package com.jsy.utils;

import lombok.Data;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
@Data
public class MyPage<T> implements Serializable {

    private static final long serialVersionUID = 8545996863226528798L;

    /**
     * 查询数据列表
     */
    protected List<T> records = Collections.emptyList();

    /**
     * 总数
     */
    protected Integer total ;
    /**
     * 每页显示条数，默认 10
     */
    protected Integer size ;

    /**
     * 当前页
     */
    protected Integer current ;

}