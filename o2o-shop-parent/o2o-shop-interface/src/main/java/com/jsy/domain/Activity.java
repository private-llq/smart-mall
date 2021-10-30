package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author lijin
 * @since 2020-11-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_activity")
@ApiModel("店铺活动实体类")
@NoArgsConstructor
@AllArgsConstructor
public class Activity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "店铺活动id",name = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "店铺活动uuid",name = "uuid")
    private String uuid;

    /**
     * 店铺id
     */
    @ApiModelProperty(value = "店铺id",name = "shopUuid")
    private String shopUuid;

    /**
     * 上限金额
     */
    @ApiModelProperty(value = "上限金额",name = "erverySum",dataType = "Integer")
    private Integer erverySum;

    /**
     * 减免金额
     */
    @ApiModelProperty(value = "减免金额",name = "reduceNum",dataType = "Integer")
    private Integer reduceNum;

    /**
     * 活动名称
     */
    @ApiModelProperty(value = "店铺活动名称",name = "name")
    private String name;

    /**
     * 是否失效 0 ：失效（撤销,过期） 1：正常（未进行，进行中） ps:过期是根据时间自动识别返回，数据库不进行维护
     */
    @ApiModelProperty(value = "店铺活动是否失效",name = "deleted",notes = "0:撤销，1:未撤销 有效")
    private Integer deleted;

    /**
     * 创建者的id
     */
    @ApiModelProperty(value = "创建者uuid",name = "userUuid")
    private String userUuid;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间",name = "beginTime",dataType = "LocalDateTime")
    private LocalDateTime beginTime;

    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间",name = "endTime",dataType = "LocalDateTime")
    private LocalDateTime endTime;

    /**
     * 活动创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间",name = "creat_time",dataType = "LocalDateTime")
    private LocalDateTime  creatTime;


    /**
     * 活动撤销时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间",name = "revokeTime",dataType = "LocalDateTime")
    private LocalDateTime  revokeTime;


    /**
     * 返回给前端的状态码 1 : 进行中 2 已撤销 3 已过期 4未开始
     */
    @TableField(exist = false)
    private Integer state;

}
