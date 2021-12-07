package com.jsy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


@Data
public class SlideshowDto implements Serializable {
    @ApiModelProperty(value = "id")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "备注")
    private String remarks;

    @ApiModelProperty(value = "图片")
    private String image;

    @ApiModelProperty(value = "标题")
    private String title;
}
