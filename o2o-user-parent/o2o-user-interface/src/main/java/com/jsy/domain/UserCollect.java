package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author yu
 * @since 2021-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_user_collect")
@ApiModel(value="UserCollect对象", description="")
public class UserCollect extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "商家id")
    private Long shopId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "套餐id")
    private Long menuId;
    /**
     * 收藏类型：0 商品、服务 1：套餐 2：商店 3：服务
     */
    private Integer type;




}
