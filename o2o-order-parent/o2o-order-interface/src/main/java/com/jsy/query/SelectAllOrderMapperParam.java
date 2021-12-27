package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @className（类名称）: SelectAllOrderMapperParam
 * @description（类描述）: this is the SelectAllOrderMapperParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/12/27
 * @version（版本）: v1.0
 */
@Data
public class SelectAllOrderMapperParam implements Serializable {
    @ApiModelProperty("开始下标")
    private Integer start;
    @ApiModelProperty("结束下标")
    private Integer end;
    @ApiModelProperty("状态")
    private Integer status;
    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;
    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;
}
