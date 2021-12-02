package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * @className（类名称）: SelectUserOrderParam
 * @description（类描述）: this is the SelectUserOrderParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/16
 * @version（版本）: v1.0
 */
@Data
public class SelectUserOrderParam {
    @ApiModelProperty("第几页")
    private Integer page;
    @ApiModelProperty("多少条数据")
    private Integer size;

    private Integer status;//(0待受理,1已受理,2待消费,3已完成,4退款中)

}
