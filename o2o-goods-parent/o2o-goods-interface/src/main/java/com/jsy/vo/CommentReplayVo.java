package com.jsy.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 会员表
 * </p>
 *
 * @author lijin
 * @since 2021-03-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_comment_replay")
public class CommentReplayVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 评论uuid
     */
    private String uuid;

    /**
     * 0->会员；1->管理员
     */
    private Integer type;

    /**
     * 会员花费的钱
     */
    private Integer number;

}
