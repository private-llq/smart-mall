package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
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
 * @since 2021-05-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_business_help")
public class BusinessHelp implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * uuid
     */
    @TableId(value = "uuid", type = IdType.AUTO)
    private String uuid;

    /**
     * 问题名字
     */
    private String helpName;

    /**
     * 解答详情
     */
    private String helpDetails;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 父级uuid
     */
    private String pid;

    /**
     * 没帮助
     */
    private Integer noHelp;

    /**
     * 有帮助
     */
    private Integer yesHelp;


    /**
     * 是否反馈（有没有点过帮助）
     */
//    private Integer hasFeedback;

    /**
     * 修改时间
     *
     */
    @ApiModelProperty(name = "updateTime", value = "更新时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private List<BusinessHelp> children;

}
