package com.jsy.basic.util.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;

@Data
public class PageQuery<T>  implements Serializable {
    //关键字
    @ApiModelProperty("查询关键字")
    private String keyword;
    //有公共属性-分页
    @ApiModelProperty("分页查询当前页")
    private Integer page = 1; //当前页
    @ApiModelProperty("分页查询每页数据条数")
    private Integer rows = 10; //每页显示多少条
    @ApiModelProperty("分页的对象")
    private T query;
}
