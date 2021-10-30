package com.jsy.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

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
public class ChatForEvaluationVo implements Serializable {

    private Long id;
    /**
     * 父级
     */
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

}
