package com.jsy.dto;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author yu
 * @since 2021-05-20
 */
@Data
@ApiModel("查询业务咨询的模块返回对象")
public class SelectBusinessConsultingDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "uuid", value = "uuid")
    private String uuid;

    @ApiModelProperty(name = "consultingName", value = "业务咨询名字")
    private String consultingName;

    @ApiModelProperty(name = "img", value = "图片")
    private String img;

    @ApiModelProperty(name = "type", value = "0业务咨询1联系客服")
    private Integer type;

    @ApiModelProperty(name = "level", value = "级别")
    private Integer level;

    @ApiModelProperty(name = "parent", value = "父级uuid")
    private String parent;

}
