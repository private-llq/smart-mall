package com.jsy.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jsy.basic.util.query.BaseQuery;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserApplySeckillQuery extends BaseQuery {
    //@JsonFormat注解的作用就是完成json字符串到java对象的转换工作，与参数传递的方向无关。
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime selectTime;
}
