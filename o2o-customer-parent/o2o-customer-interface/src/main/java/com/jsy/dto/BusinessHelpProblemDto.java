package com.jsy.dto;
import io.swagger.annotations.ApiModel;

import lombok.Data;

import java.io.Serializable;


@Data
@ApiModel("点击有无帮助")
public class BusinessHelpProblemDto implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 店铺的uuid
     */
    private String shopUuid;

    /**
     * 问题的uuid
     */
    private String helpUuid;
    /**
     * 状态
     */
    private Integer state;

}
