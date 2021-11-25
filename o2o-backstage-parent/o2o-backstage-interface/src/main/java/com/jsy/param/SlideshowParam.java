package com.jsy.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SlideshowParam implements Serializable {


    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "标题")
    private String title;

}
