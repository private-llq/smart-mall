package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className（类名称）: CreateCommentParam
 * @description（类描述）: this is the CreateCommentParam class
 * @author（创建人）: ${Administrator}
 * @createDate（创建时间）: 2021/11/22
 * @version（版本）: v1.0
 */
@Data
public class CreateCommentParam {

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "图片用,隔开")
    private String images;

    @ApiModelProperty(value = "评价信息")
    private String evaluateMessage;

    @ApiModelProperty(value = "评价星级，1星，2星，3星，4星，5星")
    private Integer evaluateLevel;
}
