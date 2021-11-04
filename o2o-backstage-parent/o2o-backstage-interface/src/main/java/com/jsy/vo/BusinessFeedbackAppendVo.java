package com.jsy.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("追加反馈意见")
public class BusinessFeedbackAppendVo {




    /**
     * 追加投诉内容
     */
    @ApiModelProperty(name = "contentAppend", value = "意见反馈内容")
    private String contentAppend;

    /**
     * 追加投诉内容图片
     */
    @ApiModelProperty(name = "picUrls", value = "意见反馈图片")
    private String[] picUrls;



    /**
     * 意见反馈订单号
     */
    @ApiModelProperty(name = "orderUuid", value = "意见反馈订单号")
    private String orderUuid;
    /**
     * 追加类型
     */
    @ApiModelProperty(name = "type", value = "追加类型")
    private String type;



}
