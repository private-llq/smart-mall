package com.jsy.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tian
 * @description:
 * @progaram: smart-mall$
 * @return: $
 * @create: 2021$  5$ 21$ $
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BusinessFeedbackDto {

    /**
     * 反馈问题（反馈类型：账户问题1、门店入驻2、产品建议3、财务结算问题4、账户安全5、其他问题6）
     */
    @ApiModelProperty(name = "feed", value = "反馈类型(1-6)")
    private String feed;

    /**
     * 意见订单uuid
     */
    @ApiModelProperty(name = "orderUuid", value = "意见订单uuid")
    private String orderUuid;

    /**
     * 追加投诉内容
     */
    @ApiModelProperty(name = "content_append", value = "追加投诉内容")
    private String contentAppend;


    /**
     * 意见反馈内容
     */
    @ApiModelProperty(name = "content", value = "意见反馈内容")
    private String content;

    /**
     * 图片路径地址
     */
    @ApiModelProperty(name = "picUrls", value = "图片路径地址")
    private List<String> picUrls;

    /**
     * 受理状态（0：未处理  1：已处理）
     */
    @ApiModelProperty(name = "state", value = "受理状态")
    private Integer state;

    /**
     * 受理状态（0：意见反馈  1：投诉建议）
     */
    @ApiModelProperty(name = "type", value = "问题类型")
    private Integer type;

    /**
     * 更新时间
     */
    @ApiModelProperty(name = "createTime", value = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
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
