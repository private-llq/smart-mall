package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.jsy.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2021-12-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("w_shop_audit")
@ApiModel(value="ShopAudit对象", description="")
public class ShopAudit extends BaseEntity {

    private static final long serialVersionUID = 1L;
//

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "审核人员id")
    private Long userId;

    @ApiModelProperty(value = "驳回理由")
    private String rejectedReason;

    @ApiModelProperty(value = "屏蔽理由")
    private String shieldingReason;

}
