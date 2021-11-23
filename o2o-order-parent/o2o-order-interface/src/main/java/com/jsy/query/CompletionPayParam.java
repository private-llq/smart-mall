package com.jsy.query;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @className（类名称）: CompletionPayParam
 * @description（类描述）: this is the CompletionPayParam class
 * @author（创建人）: ${Administrator}
 * @createDate（创建时间）: 2021/11/17
 * @version（版本）: v1.0
 */
//支付完成的接口的参数对象
@Data
public class CompletionPayParam {
    @ApiModelProperty(value = "订单id")
    private Long id;

    @ApiModelProperty(value = "支付方式（ 1app支付，2支付宝手机，3H5，4微信）")
    private Integer payType;

    @ApiModelProperty(value = "支付时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "账单月份")
    private Integer billMonth;

    @ApiModelProperty(value = "账单号")
    private String billNum;

    @ApiModelProperty(value = "账单抬头")
    private String billRise;
}
