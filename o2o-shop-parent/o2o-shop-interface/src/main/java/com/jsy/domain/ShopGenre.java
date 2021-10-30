package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@TableName("t_shop_genre")
@EqualsAndHashCode(callSuper = false)
@ApiModel("店铺类型")
public class ShopGenre implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id", value = "店铺类型id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(name = "uuid", value = "店铺类型uuid")
    private String uuid;

    @ApiModelProperty(name = "name", value = "店铺类型名称")
    private String name;
}
