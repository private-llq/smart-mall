package com.jsy.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @className（类名称）: SelectAllOrderByBackstageParam
 * @description（类描述）: this is the SelectAllOrderByBackstageParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/25
 * @version（版本）: v1.0
 */
@Data
public class SelectAllOrderByBackstageParam {
    @ApiModelProperty("第几页")
    private Integer page;
    @ApiModelProperty("多少条数据")
    private Integer size;
    @ApiModelProperty("状态")//
    private Integer status;
    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime endTime;

}
