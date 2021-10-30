package com.jsy.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author yu
 * @since 2020-12-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_suggestions")
public class Suggestions implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 描述
     */
    private String description;

    /**
     * 用户uuid或者店铺uuid
     */
    private String uuid;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 0是未处理，1已处理
     */
    private Integer isdeal;

    /**
     * 处理完成时间
     */
    private LocalDateTime finishTime;

    /**
     * 处理人uuid
     */
    private String dealmanUuid;


}
