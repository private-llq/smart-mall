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
 * @since 2021-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_chat_for_evaluation")
public class ChatForEvaluation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String parentId;

    /**
     * uuid
     */
    private String uuid;

    /**
     * 评论uuid
     */
    private String evaluationUuid;

    /**
     * 描述
     */
    private String description;

    private LocalDateTime creatTime;

    private LocalDateTime updateTime;


}
