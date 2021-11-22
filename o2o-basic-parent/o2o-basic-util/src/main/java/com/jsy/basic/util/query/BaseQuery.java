package com.jsy.basic.util.query;

import lombok.Data;

import java.io.Serializable;

/**
 * 基础查询对象
 */
@Data
public class  BaseQuery<T> implements Serializable {
    //关键字
    private String keyword;
    //有公共属性-分页
    private Integer page = 1; //当前页
    private Integer rows = 10; //每页显示多少条

}
