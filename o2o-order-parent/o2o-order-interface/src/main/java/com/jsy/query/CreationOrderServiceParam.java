package com.jsy.query;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @className（类名称）: CreationOrderServiceParam
 * @description（类描述）: this is the CreationOrderServiceParam class
 * @author（创建人）: ${Administrator}
 * @createDate（创建时间）: 2021/11/15
 * @version（版本）: v1.0
 */
@Data
public class CreationOrderServiceParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "服务 - 图片1-3张")
    private String images;
    @ApiModelProperty(value = "服务分类id(服务分类)")
    private Long goodsTypeId;
    @ApiModelProperty(value = "服务标题")
    private String title;
    @ApiModelProperty(value = "服务价格")
    private BigDecimal price;
    @ApiModelProperty(value = "折扣价格")
    private BigDecimal discountPrice;
    @ApiModelProperty(value = "是否开启折扣：0未开启 1开启")
    private Integer discountState;
    @ApiModelProperty(value = "服务的备注")
    private String textDescription;
    @ApiModelProperty(value = "服务电话")
    private String phone;
    @ApiModelProperty(value = "服务的使用规则")
    private String serviceRegulations;
    @ApiModelProperty(value = "服务有效期")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime validUntilTime;
    @ApiModelProperty(value = "服务数量")
    private Integer amount;
    @ApiModelProperty(value = "服务id")
    private Long serviceId;
}
