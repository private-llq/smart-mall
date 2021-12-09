package com.jsy.query;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className（类名称）: ReplyCommentParam
 * @description（类描述）: this is the ReplyCommentParam class
 * @author（创建人）: ${Administrator}
 * @createDate（创建时间）: 2021/12/9
 * @version（版本）: v1.0
 */
@Data
public class ReplyCommentParam {
    @ApiModelProperty(value = "评论id")
    private Long commentId;

    @ApiModelProperty(value = "回复内容")
    private String reply;
}
