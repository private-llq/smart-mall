package com.jsy.query;

import com.jsy.basic.util.query.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商家查看评论页面")
public class ShopCommentQuery extends BaseQuery {

    @ApiModelProperty
    private String shopUuid;

    @ApiModelProperty(name = "star",value = "根据总体的星级排序,默认0降序，1升序")
    private Integer star = 0; //根据总体的星级排序

}
