package com.jsy.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
public class ChatForEvaluationDto implements Serializable {

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

    /**
     * 子级
     */
    @TableField(exist = false)
    private List<ChatForEvaluationDto> list = new ArrayList<>();

    private LocalDateTime creatTime;

    private LocalDateTime updateTime;


}
