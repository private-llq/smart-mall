package com.jsy.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @author lijin
 * @since 2020-11-12
 */
@Data
@EqualsAndHashCode(callSuper = false)

@ApiModel("修改商品类型实体vo")
public class GoodsTypeUpdateVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品类型uuid
     */
    @ApiModelProperty(value = "类型uuid",name= "uuid")
    private String uuid;

    /**
     * 商品类型名称
     */
    @ApiModelProperty(value = "商品类型名称",name = "name")
    private String name;
    /**
     * 商品类型描述
     */
    @ApiModelProperty(value = "商品类型描述",name = "details")
    private String details;

}
