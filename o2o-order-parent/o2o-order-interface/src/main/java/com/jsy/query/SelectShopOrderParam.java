package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className（类名称）: SelectShopOrderParam
 * @description（类描述）: this is the SelectShopOrderParam class
 * @author（创建人）: ${arli}
 * @createDate（创建时间）: 2021/11/29
 * @version（版本）: v1.0
 */
@Data
public class SelectShopOrderParam {
    @ApiModelProperty("第几页")
    private Integer page;
    @ApiModelProperty("多少条数据")
    private Integer size;
    @ApiModelProperty("shopId")
    private Long shopId;
    private Integer status;//(0全部（预约已经成功，已经支付）,1超时警告,2售后管理,3已完成,4新的预约，5预约完成,6预约失败))
}
