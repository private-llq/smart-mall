package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2021-05-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_business_feedback")
public class BusinessFeedback implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(name = "id", value = "反馈意见id")
    private Long id;


    /**
     * 主键uuid
     */
    @ApiModelProperty(name = "uuid", value = "uuid")
    private String uuid;
    /**
     * 用户名
     */
    @ApiModelProperty(name = "userName", value = "用户名")
    private String userName;

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
     * 意见反馈uuid
     */
    @ApiModelProperty(name = "messageUuid", value = "意见反馈uuid")
    private String messageUuid;

    /**
     * 意见订单uuid
     */
    @ApiModelProperty(name = "orderUuid", value = "意见订单uuid")
    private String orderUuid;

    /**
     * 手机号
     */
    @ApiModelProperty(name = "phone", value = "手机号")
    private String phone;

    /**
     * 请求类型：0：投诉  1：反馈
     */
    @ApiModelProperty(name = "type", value = "请求类型")
    private Integer type;

    /**
     * 是否追加：1追加  0未追加
     */
    @ApiModelProperty(name = "append_type", value = "是否追加")
    private Integer appendType;

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


//    /**
//     * 追加投訴内容
//     */
//    @ApiModelProperty(name = "content_append", value = "追加投訴内容")
//    private String content_append;


    /**
     * 是否有图片（0：没有   1：有）
     */
    @ApiModelProperty(name = "hasPicture", value = "是否有图片")
    private Integer hasPicture;

    /**
     * 图片路径地址
     */
    @ApiModelProperty(name = "picUrls", value = "图片路径地址")
    private String picUrls;

    /**
     * 受理状态（0：未处理  1：已处理）
     */
    @ApiModelProperty(name = "state", value = "受理状态")
    private Integer state;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    @ApiModelProperty(name = "createTime", value = "创建时间")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */

    @ApiModelProperty(name = "updateTime", value = "更新时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime updateTime;


}
