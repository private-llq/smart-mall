package com.jsy.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Tian
 * @since 2021-11-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("服务特点名称")
public class ServiceCharacteristicsDto implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "服务特点ID",name = "ID")
    @JSONField(serializeUsing = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "服务特点名称",name = "name")
    private String name;

    @ApiModelProperty(value = "服务特点说明",name = "introduce")
    private String introduce;

//    @ApiModelProperty(value = "是否启用",name = "state")
//    private Integer state;
    @ApiModelProperty(value = "图标",name = "images")
    private String images;

}