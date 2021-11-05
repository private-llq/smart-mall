package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@EqualsAndHashCode(callSuper = false)
@TableName("t_shop_owner")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopOwner implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(name = "id",value = "账号id")
    private Long id;

    /**
     * uuid主键
     */
    @ApiModelProperty(name = "uuid",value = "账号uuid")
    private String uuid;

    /**
     * 姓名
     */
    @ApiModelProperty(name = "name",value = "姓名")
    private String name;

    /**
     * 密码
     */
    @ApiModelProperty(name = "password",value = "密码")
    private String password;

    /**
     *
     */
    @ApiModelProperty(name ="relationUuid",value = "关联用户uuid")
    private String relationUuid;

  //  @ApiModelProperty(name ="walletId",value = "电子钱包Id")
//    private String walletId;

    /**
     * 电话号码
     */
    @ApiModelProperty(name = "phone",value = "电话")
    private String phone;

    /**
     * 头像id
     */
    @ApiModelProperty(name = "headSculpture",value = "头像")
    private String headSculpture;

    /**
     * 创建时间
     */
    @ApiModelProperty(name = "creatTime",value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(name = "updateTime",value = "修改时间")
    private LocalDateTime updateTime;
}
