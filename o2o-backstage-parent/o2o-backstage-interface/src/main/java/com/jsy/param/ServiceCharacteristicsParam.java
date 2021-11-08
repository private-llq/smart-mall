package com.jsy.param;

import com.baomidou.mybatisplus.annotation.TableName;
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
public class ServiceCharacteristicsParam implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "服务特点ID",name = "ID")
    private Long id;

    @ApiModelProperty(value = "服务特点名称",name = "name")
    private String name;

    @ApiModelProperty(value = "服务特点说明",name = "introduce")
    private String introduce;

    @ApiModelProperty(value = "是否启用",name = "state")
    private Integer state;

}