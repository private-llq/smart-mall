package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 地址管理表	
 * </p>
 *
 * @author yu
 * @since 2021-11-17
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_address_management")
@ApiModel(value="AddressManagement对象", description="地址管理表	")
public class AddressManagement implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "联系人")
    private String linkman;

    @ApiModelProperty(value = "性别    0  男  |  1 女")
    private Integer sex;

    @ApiModelProperty(value = "电话")
    private String telepone;

    @ApiModelProperty(value = "地区")
    private String district;

    @ApiModelProperty(value = "详细地址")
    private String detailedAddress;

    @ApiModelProperty(value = "标签 0 家 | 1 公司 | 2 学校 ")
    private String tag;

    @ApiModelProperty(value = "默认地址")
    private String defaultAddress;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "逻辑删除   0 正常 |   1 删除")
    private Long deleted;


}
