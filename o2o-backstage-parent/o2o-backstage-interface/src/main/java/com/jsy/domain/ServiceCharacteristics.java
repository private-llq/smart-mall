package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@TableName("w_service_characteristics")
@ApiModel(value="ServiceCharacteristics对象", description="服务特点")
public class ServiceCharacteristics extends BaseEntity {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "服务特点名称",name = "name")
    private String name;

    @ApiModelProperty(value = "服务特点说明",name = "introduce")
    private String introduce;

    @ApiModelProperty(value = "是否启用",name = "state")
    private Integer state;

}
