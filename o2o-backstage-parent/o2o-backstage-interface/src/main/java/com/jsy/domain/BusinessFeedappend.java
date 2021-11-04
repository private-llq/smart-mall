package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("t_business_freeappend")
public class BusinessFeedappend implements Serializable {

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
     * 意见订单uuid
     */
    @ApiModelProperty(name = "orderUuid", value = "意见订单uuid")
    private String orderUuid;


    /**
     * 请求类型：0：投诉  1：反馈
     */
    @ApiModelProperty(name = "type", value = "请求类型")
    private Integer type;



    /**
     * 意见反馈内容
     */
    @ApiModelProperty(name = "contentAppend", value = "意见反馈内容")
    private String contentAppend;




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
     * 追加时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    @ApiModelProperty(name = "append_time", value = "追加时间")
    private LocalDateTime appendTime;




}
