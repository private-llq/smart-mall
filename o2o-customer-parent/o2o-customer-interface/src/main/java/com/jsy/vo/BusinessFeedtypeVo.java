package com.jsy.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("新增反馈意见")
public class BusinessFeedtypeVo implements Serializable{


    /**
     * 用户名
     */
    @ApiModelProperty(name = "username", value = "用户名")
    private String username;

    /**
     * 用户uuid
     */
    @ApiModelProperty(name = "userUuid", value = "用户uuid")
    private String userUuid;

    /**
     * 店铺名称
     */
    @ApiModelProperty(name = "shopName", value = "用户uuid")
    private String shopName;

    /**
     * 店铺uuid
     */
    @ApiModelProperty(name = "info_uuid", value = "店铺uuid")
    private String info_uuid;

    /**
     * 手机号
     */
    @ApiModelProperty(name = "phone", value = "手机号")
    private String phone;


    /**
     * 反馈类型（反馈类型：账户问题1、门店入驻2、产品建议3、财务结算问题4、账户安全5、其他问题6）
     */
    @ApiModelProperty(name = "feedType", value = "反馈类型(1-6)")
    private Integer feedType;

    /**
     * 意见反馈内容
     */
    @ApiModelProperty(name = "content", value = "意见反馈内容")
    private String content;

    /**
     * 意见反馈图片
     */
    @ApiModelProperty(name = "images", value = "意见反馈图片")
    private List<String> picUrls;


    /**
     * 请求类型：0：投诉  1：反馈
     */
    @ApiModelProperty(name = "type", value = "请求类型")
    private Integer type;


}
